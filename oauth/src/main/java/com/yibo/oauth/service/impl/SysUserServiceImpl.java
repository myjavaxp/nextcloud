package com.yibo.oauth.service.impl;

import com.yibo.entity.oauth.Resource;
import com.yibo.entity.oauth.Role;
import com.yibo.entity.oauth.SysUser;
import com.yibo.entity.oauth.UserAuthorization;
import com.yibo.oauth.dao.SysUserMapper;
import com.yibo.oauth.service.CacheService;
import com.yibo.oauth.service.SysUserService;
import com.yibo.oauth.utils.SHA256Util;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.yibo.oauth.constants.TokenConstant.*;
import static io.jsonwebtoken.SignatureAlgorithm.HS256;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service("sysUserService")
public class SysUserServiceImpl implements SysUserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SysUserServiceImpl.class);
    private final SysUserMapper sysUserMapper;
    private final StringRedisTemplate redisTemplate;
    private final CacheService cacheService;

    @Autowired
    public SysUserServiceImpl(SysUserMapper sysUserMapper, StringRedisTemplate redisTemplate, CacheService cacheService) {
        this.sysUserMapper = sysUserMapper;
        this.redisTemplate = redisTemplate;
        this.cacheService = cacheService;
    }

    @Override
    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public UserAuthorization login(SysUser sysUser, HttpServletResponse response) {
        UserAuthorization userAuthorization;
        String username = sysUser.getUsername();
        Assert.hasText(username, "用户名不能为空");
        Assert.hasText(sysUser.getPassword(), "密码不能为空");
        SysUser user = cacheService.selectByUsername(username);
        Assert.notNull(user, "用户名不存在");
        Assert.isTrue(SHA256Util.getSHA256(sysUser.getPassword()).equals(user.getPassword()), "密码错误");
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String token = valueOperations.get(username);
        if (token != null) {
            String signingKey = valueOperations.get(token);
            Claims claims = Jwts.parser()
                    .setSigningKey(signingKey)
                    .parseClaimsJws(token)
                    .getBody();
            String usernameInToken = claims.getSubject();
            Assert.isTrue(username.equals(usernameInToken), "Token解析用户名不一致");
            List<Role> roleList = (List<Role>) claims.get(ROLE_LIST);
            List<Resource> resourceList = (List<Resource>) claims.get(RESOURCE_LIST);
            userAuthorization = new UserAuthorization(user.getId(), username, roleList, resourceList);
            LOGGER.info("用户 ->{}<- 从Redis获取token", username);
            redisTemplate.expire(username, TOKEN_REDIS_EXPIRATION, TimeUnit.SECONDS);
            redisTemplate.expire(token, TOKEN_REDIS_EXPIRATION, TimeUnit.SECONDS);
        } else {
            List<Role> roleList = sysUserMapper.selectRolesByUserId(user.getId());
            List<Resource> resourceList = sysUserMapper.selectResourceListByRoleList(roleList);
            userAuthorization = new UserAuthorization(user.getId(), username, roleList, resourceList);
            token = getToken(username, userAuthorization, valueOperations);
        }
        response.setHeader(AUTHORIZATION, BEARER + token);
        return userAuthorization;
    }

    private String getToken(String username, UserAuthorization authorization, ValueOperations<String, String> valueOperations) {
        String signingKey = username + System.currentTimeMillis();
        Claims claims = Jwts.claims().setSubject(username);
        claims.put(ROLE_LIST, authorization.getRoleList());
        claims.put(RESOURCE_LIST, authorization.getResourceList());
        String token = Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION)) //过期时间15天
                .signWith(HS256, signingKey)
                .compact();
        valueOperations.set(username, token);
        valueOperations.set(token, signingKey);
        redisTemplate.expire(username, TOKEN_REDIS_EXPIRATION, TimeUnit.SECONDS);
        redisTemplate.expire(token, TOKEN_REDIS_EXPIRATION, TimeUnit.SECONDS);
        return token;
    }

    @Override
    public void logout(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION);
        if (null == header) {
            return;
        }
        String token = header.replace(BEARER, "");
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String signingKey = valueOperations.get(token);
        Claims claims = Jwts.parser()
                .setSigningKey(signingKey)
                .parseClaimsJws(token)
                .getBody();
        String username = claims.getSubject();
        redisTemplate.delete(username);
        redisTemplate.delete(token);
    }
}
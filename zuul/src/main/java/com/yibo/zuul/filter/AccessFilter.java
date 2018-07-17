package com.yibo.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.yibo.constants.TokenConstant.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
public class AccessFilter extends ZuulFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccessFilter.class);
    private final StringRedisTemplate stringRedisTemplate;

    @Autowired
    public AccessFilter(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object run() throws ZuulException {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        HttpServletResponse response = requestContext.getResponse();
        String requestURL = request.getRequestURL().toString();
        LOGGER.info("send {} request to {}", request.getMethod(), requestURL);
        if (requestURL.contains("login") || requestURL.contains("logout")) {
            return null;
        }
        String authorization = request.getHeader(AUTHORIZATION);
        if (null == authorization) {
            LOGGER.error("Token值为空");
            throw new ZuulException("请登陆后再操作", 401, "Token为空");
        }
        String token = authorization.replace(BEARER, "");
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        String signingKey = valueOperations.get(token);
        if (null == signingKey) {
            LOGGER.error("Token已过期");
            throw new ZuulException("请登陆后再操作", 401, "Token已过期");
        }
        Claims claims = Jwts.parser()
                .setSigningKey(signingKey)
                .parseClaimsJws(token)
                .getBody();
        String username = claims.getSubject();
        if (!token.equals(valueOperations.get(username))) {
            LOGGER.error("Token不一致");
            throw new ZuulException("请登陆后再操作", 401, "Token不一致");
        }
        List<LinkedHashMap> resourceList = (List<LinkedHashMap>) claims.get(RESOURCE_LIST);
        String userId = claims.get(USER_ID).toString();
        List<String> urlList = new ArrayList<>();
        for (LinkedHashMap linkedHashMap : resourceList) {
            if (linkedHashMap.get("type").equals(1)) {
                urlList.add(linkedHashMap.get("content").toString());
            }
        }
        if (urlList.stream().anyMatch(a -> a.equals("*"))) {
            stringRedisTemplate.expire(username, TOKEN_REDIS_EXPIRATION, TimeUnit.SECONDS);
            stringRedisTemplate.expire(token, TOKEN_REDIS_EXPIRATION, TimeUnit.SECONDS);
            requestContext.addZuulRequestHeader(AUTHORIZATION,userId + "," + username);
            return null;
        }
        if (urlList.stream().noneMatch(requestURL::contains)) {
            LOGGER.error("没有访问权限");
            throw new ZuulException("请登陆后再操作", 403, "没有访问权限");
        }
        stringRedisTemplate.expire(username, TOKEN_REDIS_EXPIRATION, TimeUnit.SECONDS);
        stringRedisTemplate.expire(token, TOKEN_REDIS_EXPIRATION, TimeUnit.SECONDS);
        requestContext.addZuulRequestHeader(AUTHORIZATION,userId + "," + username);
        return null;
    }
}
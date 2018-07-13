package com.yibo.oauth.service.impl;

import com.yibo.entity.oauth.SysUser;
import com.yibo.oauth.dao.SysUserMapper;
import com.yibo.oauth.service.CacheService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service("cacheService")
public class CacheServiceImpl implements CacheService {
    @Resource
    private SysUserMapper sysUserMapper;
    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "userPass",key = "#username")
    public SysUser selectByUsername(String username){
        return sysUserMapper.selectByUsername(username);
    }
}
package com.yibo.oauth.service;

import com.yibo.entity.oauth.SysUser;

public interface CacheService {
    SysUser selectByUsername(String username);
}
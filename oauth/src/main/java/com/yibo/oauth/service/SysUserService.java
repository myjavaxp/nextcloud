package com.yibo.oauth.service;

import com.yibo.entity.oauth.SysUser;
import com.yibo.entity.oauth.UserAuthorization;

import javax.servlet.http.HttpServletResponse;

public interface SysUserService {
    UserAuthorization login(SysUser sysUser, HttpServletResponse response);
}
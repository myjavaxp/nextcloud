package com.yibo.utils;

import com.yibo.entity.oauth.SysUser;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class TokenUtil {
    public static SysUser getUser(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION);
        if (null == header) {
            return new SysUser(0L, "system");
        }
        int index = header.indexOf(",");
        Long id = Long.parseLong(header.substring(0, index));
        String username = header.substring(index + 1, header.length());
        return new SysUser(id, username);
    }
}
package com.yibo.oauth.controller;

import com.yibo.entity.common.ResponseEntity;
import com.yibo.entity.oauth.SysUser;
import com.yibo.entity.oauth.UserAuthorization;
import com.yibo.oauth.service.SysUserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@SuppressWarnings("unchecked")
public class LogController {
    @Resource
    private SysUserService sysUserService;

    @PostMapping("/login")
    public ResponseEntity<UserAuthorization> login(@RequestBody SysUser sysUser, HttpServletResponse response) {
        return ResponseEntity.ofSuccessData(sysUserService.login(sysUser, response));
    }

    @GetMapping("/logout")
    public ResponseEntity logout(HttpServletRequest request) {
        sysUserService.logout(request);
        return ResponseEntity.ofSuccessMessage("退出登陆成功");
    }
}
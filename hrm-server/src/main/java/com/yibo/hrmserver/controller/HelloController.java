package com.yibo.hrmserver.controller;

import com.yibo.entity.oauth.SysUser;
import com.yibo.utils.TokenUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/hello")
public class HelloController {
    @GetMapping("/name/{name}")
    public Map<String, String> hello(@PathVariable("name") String name, HttpServletRequest request) {
        SysUser user = TokenUtil.getUser(request);
        Map<String, String> map = new HashMap<>();
        map.put("hello", name);
        map.put("userId", user.getId().toString());
        map.put("username", user.getUsername());
        return map;
    }

    @GetMapping("/guest/{name}")
    public Map<String, String> guest(@PathVariable("name") String name, HttpServletRequest request) {
        SysUser sysUser = TokenUtil.getUser(request);
        Map<String, String> map = new HashMap<>();
        map.put("userId", sysUser.getId().toString());
        map.put("username", sysUser.getUsername());
        return map;
    }
}
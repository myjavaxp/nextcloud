package com.yibo.oauth.controller;

import com.yibo.oauth.entity.SysUser;
import com.yibo.oauth.service.TestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Yibo
 */
@RestController
public class TestController {
    @Resource
    private TestService testService;
    @GetMapping("/test/{id}")
    public SysUser getUserById(@PathVariable("id") Long id){
        return testService.getUserById(id);
    }
}

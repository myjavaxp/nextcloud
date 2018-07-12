package com.yibo.hrmserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/hello")
public class HelloController {
    @GetMapping("/name/{name}")
    public Map<String,String> hello(@PathVariable("name") String name){
        Map<String,String> map=new HashMap<>();
        map.put("hello",name);
        return map;
    }
}
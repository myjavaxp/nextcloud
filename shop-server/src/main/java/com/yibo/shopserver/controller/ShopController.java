package com.yibo.shopserver.controller;

import com.yibo.entity.common.ResponseEntity;
import com.yibo.shopserver.feign.HelloClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
public class ShopController {
    @Resource
    private HelloClient helloClient;

    @GetMapping("/hi/{name}")
    public ResponseEntity<Map<String, String>> hi(@PathVariable("name") String name) {
        Map<String, String> guest = helloClient.guest(name);
        guest.putAll(helloClient.hello(name));
        return new ResponseEntity<>(guest);
    }

    @GetMapping("/member/{id}")
    public ResponseEntity<Map<String, String>> member(@PathVariable("id") Long id) {
        return helloClient.member(id);
    }
}
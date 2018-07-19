package com.yibo.shopserver.hystrix;

import com.yibo.shopserver.feign.HelloClient;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Component
public class HelloClientFallback implements FallbackFactory<HelloClient> {
    @Override
    public HelloClient create(Throwable cause) {
        return new HelloClient() {
            @Override
            public Map<String, String> guest(String name, HttpServletRequest request) {
                Map<String, String> map = new HashMap<>();
                map.put("hello", name);
                return map;
            }

            @Override
            public Map<String, String> hello(String name, HttpServletRequest request) {
                Map<String, String> map = new HashMap<>();
                map.put("hi", name);
                return map;
            }
        };
    }
}
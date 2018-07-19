package com.yibo.shopserver.hystrix;

import com.yibo.shopserver.feign.HelloClient;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class HelloClientFallback implements FallbackFactory<HelloClient> {
    @Override
    public HelloClient create(Throwable cause) {
        return new HelloClient() {
            @Override
            public Map<String, String> guest(String name) {
                Map<String, String> map = new HashMap<>();
                map.put("熔断", name);
                return map;
            }

            @Override
            public Map<String, String> hello(String name) {
                Map<String, String> map = new HashMap<>();
                map.put("异常", name);
                return map;
            }
        };
    }
}
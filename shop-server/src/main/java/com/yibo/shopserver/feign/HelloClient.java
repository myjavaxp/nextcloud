package com.yibo.shopserver.feign;

import com.yibo.entity.common.ResponseEntity;
import com.yibo.shopserver.hystrix.HelloClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(value = "hrm-server", fallbackFactory = HelloClientFallback.class)
public interface HelloClient {
    @GetMapping(value = "hello/guest/{name}")
    Map<String, String> guest(@PathVariable("name") String name);

    @GetMapping(value = "hello/name/{name}")
    Map<String, String> hello(@PathVariable("name") String name);

    @GetMapping(value = "hello/member/{id}")
    ResponseEntity<Map<String, String>> member(@PathVariable("id") Long id);
}
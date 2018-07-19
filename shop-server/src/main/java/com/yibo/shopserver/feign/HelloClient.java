package com.yibo.shopserver.feign;

import com.yibo.shopserver.hystrix.HelloClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@FeignClient(value = "hrm-server",url = "http://127.0.0.1:8080/", fallbackFactory = HelloClientFallback.class)
public interface HelloClient {
    @GetMapping("/guest/{name}")
    Map<String, String> guest(@PathVariable("name") String name, HttpServletRequest request);

    @GetMapping("/name/{name}")
    Map<String, String> hello(@PathVariable("name") String name, HttpServletRequest request);
}
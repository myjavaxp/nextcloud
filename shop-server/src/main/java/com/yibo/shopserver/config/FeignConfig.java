package com.yibo.shopserver.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Configuration
public class FeignConfig implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(requestAttributes).getRequest();
        String header = request.getHeader(AUTHORIZATION);
        if (header == null || "".equals(header) || !header.contains(",")) {
            header = "0,system";
        }
        requestTemplate.header(AUTHORIZATION, header);
    }
}
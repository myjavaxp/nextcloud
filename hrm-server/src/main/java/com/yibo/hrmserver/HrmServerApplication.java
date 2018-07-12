package com.yibo.hrmserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class HrmServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(HrmServerApplication.class, args);
    }
}
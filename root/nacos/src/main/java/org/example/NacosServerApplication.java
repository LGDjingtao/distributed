package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient //nacos发现注解
public class NacosServerApplication {



    public static void main(String[] args) {
        SpringApplication.run(NacosServerApplication.class, args);
    }
}

package com.imooc.springcloud;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
public class FeignConsumerApp {
    public static void main(String[] args) {
        new SpringApplicationBuilder(FeignConsumerApp.class)
                .web(WebApplicationType.SERVLET)
                .run(args);
    }
}

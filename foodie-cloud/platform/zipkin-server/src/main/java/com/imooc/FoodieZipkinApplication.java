package com.imooc;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import zipkin.server.internal.EnableZipkinServer;

@SpringBootApplication
@EnableZipkinServer
@EnableDiscoveryClient
public class FoodieZipkinApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(FoodieZipkinApplication.class)
                .web(WebApplicationType.SERVLET)
                .run(args);
    }
}

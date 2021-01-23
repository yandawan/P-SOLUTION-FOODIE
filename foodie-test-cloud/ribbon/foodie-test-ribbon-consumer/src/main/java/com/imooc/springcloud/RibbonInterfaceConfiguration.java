package com.imooc.springcloud;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 使用代码方式: 针对eureka-client 使用负载均衡
 */
@Configuration
@RibbonClient(name = "eureka-client")
public class RibbonInterfaceConfiguration {
    @Bean
    public IRule defaultLBStrategy() {
        return new RandomRule();
    }
}

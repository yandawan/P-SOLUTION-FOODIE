package com.imooc.springcloud;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 全局使用负载均衡算法
 */
@Configuration
@RibbonClient()
public class RibbonConfiguration {
    @Bean
    public IRule defaultLBStrategy() {
        return new RandomRule();
    }
}

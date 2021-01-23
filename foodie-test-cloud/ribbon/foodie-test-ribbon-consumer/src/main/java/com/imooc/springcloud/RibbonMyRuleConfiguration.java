package com.imooc.springcloud;

import com.imooc.springcloud.rules.MyRule;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 使用代码方式: 针对eureka-client 使用自定义的负载均衡
 */
@Configuration
@RibbonClient(name = "eureka-client", configuration = MyRule.class)
public class RibbonMyRuleConfiguration {
}

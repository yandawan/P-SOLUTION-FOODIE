package com.bfxy.test;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	// 降级
	public static void intDegradeRules() {
		List<DegradeRule> rules = new ArrayList<DegradeRule>();
		DegradeRule rule = new DegradeRule();
		//	规则一定要绑定到对应的资源上，通过资源名称进行绑定
		rule.setResource("com.bfxy.test.web.IndexController:degrade");
		rule.setGrade(RuleConstant.DEGRADE_GRADE_EXCEPTION_COUNT);
		rule.setCount(2);
		rules.add(rule);
		// 规则管理器
		DegradeRuleManager.loadRules(rules);
	}

	// 限流
	public static void intFlowRules() {
		List<FlowRule> rules = new ArrayList<FlowRule>();
		FlowRule rule = new FlowRule();
		//	注意： 我们的规则一定要绑定到对应的资源上，通过资源名称进行绑定
		rule.setResource("helloWorld");
		rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
		rule.setCount(20);
		rules.add(rule);
		// 规则管理器
		FlowRuleManager.loadRules(rules);
	}
	
	public static void main(String[] args) {
		// 1 引入对应的依赖
		// 2 定义资源
		// 3 定义规则
		// 4 查看结果
		// 5 控制台
		SpringApplication.run(Application.class, args);
		intFlowRules();
        intDegradeRules();
		System.err.println("规则加载完毕!");
	}
	
}

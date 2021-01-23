package com.bfxy.test.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;

import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class IndexController {

	private static final String RESOURCE_NAME= "helloWorld";

	@RequestMapping("/flow")
	public String flow() throws InterruptedException {
		Entry entry = null;
		try {
			//	2.1 定义资源名称
			entry = SphU.entry(RESOURCE_NAME);
			
			//	2.2 执行资源逻辑代码
			System.err.println(RESOURCE_NAME+": 访问数据库");
			System.err.println(RESOURCE_NAME+":: 访问远程redis");
			System.err.println(RESOURCE_NAME+":: 数据库持久化操作");
			Thread.sleep(20);
			
		} catch (BlockException e) {
			System.err.println("要访问的资源被流控了, 执行流控逻辑！");
		} finally {
			if(entry != null) {
				entry.exit();
			}
		}
		return "flow";
	}

	private AtomicInteger count = new AtomicInteger(0);

	@RequestMapping("/degrade")
	public String degrade() throws Exception {
		Entry entry = null;
		try {
			// resource name:
			String resouceName = "com.bfxy.test.web.IndexController:degrade";
			//	2.1 定义资源名称
			entry = SphU.entry(resouceName);
			//	2.2 执行资源逻辑代码
			count.incrementAndGet();
			if(count.get() % 2 == 0) {
				Thread.sleep(100);
				System.err.println("degrade--> 执行正常 100 ms ");
				//throw new Exception("degrade--> 抛出异常");
			} else {
				Thread.sleep(20);
				System.err.println("degrade--> 执行正常 20 ms ");
			}
		} catch (BlockException e) {
			System.err.println("要访问的资源被降级了, 执行降级逻辑！");
		} finally {
			if(entry != null) {
				entry.exit();
			}
		}
		return "degrade";
	}
}


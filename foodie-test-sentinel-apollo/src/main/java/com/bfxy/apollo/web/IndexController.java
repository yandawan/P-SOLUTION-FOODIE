package com.bfxy.apollo.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bfxy.apollo.JavaConfigBean;
import com.bfxy.apollo.service.FlowService;

@RestController
public class IndexController {

	@Value("${port:1}")
	private String port;

	@Autowired
	private JavaConfigBean javaConfigBean;
	
	@RequestMapping("/index")
	public String index() {
		System.err.println("timeout: " + javaConfigBean.getTimeout());
		System.err.println("newKey: " + javaConfigBean.getNewKey());
		System.err.println("port: " + port);

		return "index";
	}
	
	@Autowired
	private FlowService flowService;
	
	@RequestMapping("/test")
	public String test() {
		return flowService.test();
	}
	
	
}

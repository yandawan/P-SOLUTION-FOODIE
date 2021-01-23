package com.bfxy.test.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bfxy.test.service.DegradeService;
import com.bfxy.test.service.FlowService;

@RestController
public class SentinelAnnotationController {

	@Autowired
	private FlowService flowService;

	@Autowired
	private DegradeService degradeService;

	@RequestMapping("/flow-test")
	public String flowTest() {
		return flowService.flow();
	}

	@RequestMapping("/degrade-test")
	public String degradeTest() {
		return degradeService.degrade();
	}


}

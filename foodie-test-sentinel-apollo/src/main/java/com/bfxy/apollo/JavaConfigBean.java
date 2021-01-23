package com.bfxy.apollo;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class JavaConfigBean {

	@Value("${timeout:20}")
	private int timeout;
	
	@Value("${newKey:'hello'}")
	private String newKey;
}

package com.imooc.springcloud;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class Controller {

    @Autowired
    private AccessLimiter accessLimiter;

    @GetMapping("test")
    public String test() {
        accessLimiter.limitAccess("ratelimiter-test", 3);
        return "success";
    }

    @GetMapping("test-annotation")
    @com.imooc.springcloud.annotation.AccessLimiter(limit = 1)
    public String testAnnotation() {
        return "success";
    }

}

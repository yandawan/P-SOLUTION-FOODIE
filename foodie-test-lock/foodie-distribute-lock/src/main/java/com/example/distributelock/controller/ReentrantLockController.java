package com.example.distributelock.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@RestController
@Slf4j
public class ReentrantLockController {

    private final Lock lock = new ReentrantLock();

    @RequestMapping("singleLock")
    public String singleLock() throws Exception {
        lock.lock();
        log.info("我进入了锁！");
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        lock.unlock();
        return "我已经执行完成！";
    }
}

package com.example.distributelock;

import com.example.distributelock.lock.ZkLock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DistributeLockApplicationTests {

    @Test
    public void testZkLock() throws IOException {
        ZkLock zkLock = new ZkLock("localhost:2181","order");
        try {
            zkLock.getLock();
            zkLock.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

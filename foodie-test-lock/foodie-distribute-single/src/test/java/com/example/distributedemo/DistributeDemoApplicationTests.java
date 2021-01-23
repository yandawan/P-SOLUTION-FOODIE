package com.example.distributedemo;

import com.example.distributedemo.service.OrderServiceReentrantLock;
import com.example.distributedemo.service.OrderServiceSynchronized1;
import com.example.distributedemo.service.OrderServiceSynchronized2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DistributeDemoApplicationTests {
    @Autowired
    private OrderServiceReentrantLock orderServiceReentrantLock;
    @Autowired
    private OrderServiceSynchronized1 orderServiceSynchronized1;
    @Autowired
    private OrderServiceSynchronized2 orderServiceSynchronized2;

    @Test
    public void concurrentOrderSynchronized1() throws InterruptedException {
        CountDownLatch cdl = new CountDownLatch(5);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(5);

        ExecutorService es = Executors.newFixedThreadPool(5);
        for (int i =0;i<5;i++){
            es.execute(()->{
                try {
                    cyclicBarrier.await();
                    Integer orderId = orderServiceSynchronized1.createOrder();
                    System.out.println("订单id："+orderId);
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    cdl.countDown();
                }
            });
        }
        cdl.await();
        es.shutdown();
    }

    @Test
    public void concurrentOrderSynchronized2() throws InterruptedException {
        CountDownLatch cdl = new CountDownLatch(5);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(5);

        ExecutorService es = Executors.newFixedThreadPool(5);
        for (int i =0;i<5;i++){
            es.execute(()->{
                try {
                    cyclicBarrier.await();
                    Integer orderId = orderServiceSynchronized2.createOrder();
                    System.out.println("订单id："+orderId);
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    cdl.countDown();
                }
            });
        }
        cdl.await();
        es.shutdown();
    }

    @Test
    public void orderServiceReentrantLockTest() throws InterruptedException {
        CountDownLatch cdl = new CountDownLatch(5);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(5);

        ExecutorService es = Executors.newFixedThreadPool(5);
        for (int i =0;i<5;i++){
            es.execute(()->{
                try {
                    cyclicBarrier.await();
                    Integer orderId = orderServiceReentrantLock.createOrder();
                    System.out.println("订单id："+orderId);
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    cdl.countDown();
                }
            });
        }
        cdl.await();
        es.shutdown();
    }

}

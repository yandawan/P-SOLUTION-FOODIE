package com.bfxy.rabbit.producer.broker;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

@Slf4j
public class AsyncBaseQueue {

    // 线程数
    private static final int THREAD_SIZE = Runtime.getRuntime().availableProcessors();

    // 队列大小
    private static final int QUEUE_SIZE = 10000;

    // 定义线程池
    private static ExecutorService senderAsync =
            new ThreadPoolExecutor(THREAD_SIZE, THREAD_SIZE,60L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(QUEUE_SIZE),
                    r -> {
                        Thread t = new Thread(r);
                        t.setName("rabbitMQ_client_async_sender");
                        return t;
                    },
                    (r, executor) -> log.error("async sender is error rejected, runnable: {}, executor: {}", r, executor));
    // 提交
    public static void submit(Runnable runnable) {
        senderAsync.submit(runnable);
    }

}

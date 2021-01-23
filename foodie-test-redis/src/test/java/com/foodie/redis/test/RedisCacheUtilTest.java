package com.foodie.redis.test;

import com.foodie.redis.RedisApplication;
import com.foodie.redis.config.RedisCacheUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RedisApplication.class})
@SpringBootTest
public class RedisCacheUtilTest {
    @Autowired
    RedisCacheUtil redisCacheUtil;

    @Test
    public void putTest(){
        String key = "foodie-test-redis:";
        String childKey = "putTest";
        String value = "hello world";
        redisCacheUtil.put(key,childKey,value);
        System.out.println(redisCacheUtil.get(key,childKey));
    }
}

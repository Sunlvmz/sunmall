package com.sun.sunmall.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.Assert.*;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-*.xml"})

public class RedisCacheTest {
    @Autowired
    private RedisCache redisCache;

    @Test
    public void putCache() throws Exception {
        String result = redisCache.putCache("test", "helloworld");
        System.out.println(result);
        String s = redisCache.getCache("test", String.class);
        System.out.println(s);
    }

}
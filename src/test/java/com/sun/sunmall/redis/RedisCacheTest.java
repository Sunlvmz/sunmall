package com.sun.sunmall.redis;

import com.sun.sunmall.dao.ProductMapper;
import com.sun.sunmall.pojo.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-*.xml"})

public class RedisCacheTest {
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ProductMapper productMapper;
    @Test
    public void putCache() throws Exception {
        String result = redisCache.putCache("test", "helldfsfsdfsoworld");
        System.out.println(result);
        String s = redisCache.getCache("test", String.class);
        System.out.println(s);
    }
    @Test
    public void zaddList() throws Exception {
        List<Product> products = productMapper.selectProductList();
        redisCache.zaddList("PriceTop",products,products.get())
    }

}
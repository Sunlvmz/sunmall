package com.sun.sunmall.redis;

import com.scut.seckill.entity.Product;
import com.scut.seckill.mapper.SecKillMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@Component
public class RedisLoaderListener {

    @Autowired
    private RedisCacheHandle redisCacheHandle;

    @Autowired
    private SecKillMapper secKillMapper;


    //此注解用于在会在服务器加载Servlet的时候运行，并且只会被服务器调用一次，类似于Servlet的init()方法。
    // 被@PostConstruct修饰的方法会在构造函数之后，init()方法之前运行。
    @PostConstruct
    public void initRedis(){
        Jedis jedis = redisCacheHandle.getJedis();
        //清空Redis缓存
        jedis.flushDB();
        List<Product> productList = secKillMapper.getAllProduct();
        for (Product product:productList) {
            jedis.set("product_"+product.getId(),product.getProductName());
            jedis.set(product.getProductName()+"_stock", String.valueOf(product.getStock()));
        }
        log.info("Redis缓存数据初始化完毕！");
    }
}

package com.sun.sunmall.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.util.Hashing;
import redis.clients.util.Sharded;

import java.util.ArrayList;
import java.util.List;

/**
 * @author twc
 */
@Configuration
@Slf4j
public class RedisCacheConfig {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis2.host}")
    private String host2;

    @Value("${spring.redis2.port}")
    private int port2;

    @Value("${spring.redis.pool.max-idle}")
    private int maxIdle;

    @Value("${spring.redis.pool.min-idle}")
    private int minIdle;

    @Value("${spring.redis.pool.max-wait}")
    private long maxWaitMillis;

    @Value("${spring.redis.pool.max-active}")
    private int maxActive;

    @Value("${spring.redis.timeout}")
    private int timeout;

    @Value("${spring.redis.database}")
    private int database;


    @Bean(name = "poolConfig")
    public JedisPoolConfig initJedisPoolConfig(){
        log.info("JedisPoolConfig注入开始:");
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(maxActive);
        poolConfig.setMaxIdle(maxIdle);
        poolConfig.setMaxWaitMillis(maxWaitMillis);
        poolConfig.setMinIdle(minIdle);
        poolConfig.setTestOnBorrow(false); //否则发生jedis could not get a pool 异常
        poolConfig.setTestOnReturn(true);
        poolConfig.setBlockWhenExhausted(true);//链接耗尽是否堵塞。 TURE则堵塞直到超时，抛出异常
        return poolConfig;
    }

    @Bean
    public JedisPool initJedisPool(@Qualifier("poolConfig") JedisPoolConfig poolConfig){
        log.info("JedisPool注入开始:");
        JedisPool jedisPool = new JedisPool(poolConfig,host,port,timeout);
        return jedisPool;
    }
    @Bean
    public ShardedJedisPool initShardedJedisPool(@Qualifier("poolConfig") JedisPoolConfig poolConfig){
        log.info("ShardedJedisPool注入开始:");
        JedisShardInfo info1 = new JedisShardInfo(host,port,1000*2);

        JedisShardInfo info2 = new JedisShardInfo(host2,port2,1000*2);


        List<JedisShardInfo> jedisShardInfoList = new ArrayList<JedisShardInfo>(2);

        jedisShardInfoList.add(info1);
        jedisShardInfoList.add(info2);

        ShardedJedisPool pool = new ShardedJedisPool(poolConfig,jedisShardInfoList, Hashing.MURMUR_HASH, Sharded.DEFAULT_KEY_TAG_PATTERN);
        return pool;
    }

}

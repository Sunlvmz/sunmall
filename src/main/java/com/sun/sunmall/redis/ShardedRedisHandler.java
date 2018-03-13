package com.sun.sunmall.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

@Component
public class ShardedRedisHandler {

    @Autowired
    private ShardedJedisPool shardedJedisPool;

    public ShardedJedis getShardedJedis(){
        return shardedJedisPool.getResource();
    }
}

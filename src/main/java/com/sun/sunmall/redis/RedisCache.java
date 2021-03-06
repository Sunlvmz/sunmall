package com.sun.sunmall.redis;

import java.util.List;
import java.util.Set;

import com.sun.sunmall.annotation.Redis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.sun.sunmall.utils.ProtoStuffSerializerUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * redis缓存
 * @author lushaoqing
 */
@Component
public class RedisCache {

    public final static String CAHCENAME="cache";//缓存名
    public final static int CAHCETIME=60;//默认缓存时间

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private JedisPool jedisPool;
    /**
     * 存入redis，不带过期时间
     */
    public <T> String putCache(String key, T obj) {
        final byte[] bkey = key.getBytes();
        final byte[] bvalue = ProtoStuffSerializerUtil.serialize(obj);
        Jedis jedis = jedisPool.getResource();
        String result = jedis.set(bkey, bvalue);
        jedis.close();
        return result;
    }

    /**
     * 存入redis，带过期时间
     */
    public <T> void putCacheWithExpireTime(String key, T obj, final long expireTime) {
        final byte[] bkey = key.getBytes();
        final byte[] bvalue = ProtoStuffSerializerUtil.serialize(obj);
        redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                connection.setEx(bkey, expireTime, bvalue);
                return true;
            }
        });
    }

    public <T> boolean putListCache(String key, List<T> objList) {
        final byte[] bkey = key.getBytes();
        final byte[] bvalue = ProtoStuffSerializerUtil.serializeList(objList);
        boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.setNX(bkey, bvalue);
            }
        });
        return result;
    }

    public <T> boolean putListCacheWithExpireTime(String key, List<T> objList, final long expireTime) {
        final byte[] bkey = key.getBytes();
        final byte[] bvalue = ProtoStuffSerializerUtil.serializeList(objList);
        boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                connection.setEx(bkey, expireTime, bvalue);
                return true;
            }
        });
        return result;
    }

//    public <T> T getCache(final String key, Class<T> targetClass) {
//        byte[] result = redisTemplate.execute(new RedisCallback<byte[]>() {
//            @Override
//            public byte[] doInRedis(RedisConnection connection) throws DataAccessException {
//
//              byte[] data = connection.get(key.getBytes());
//              connection.close();   //没这个会出现 cannot get a resource from pool
//              return data;
//            }
//        });
//        if (result == null) {
//            return null;
//        }
//        return ProtoStuffSerializerUtil.deserialize(result, targetClass);
//    }
     public <T> T getCache(final String key, Class<T> targetClass) {
         final byte[] bkey = key.getBytes();
         Jedis jedis = jedisPool.getResource();
         byte[] result = jedis.get(bkey);
         if (result == null) {
             return null;
         }
         jedis.close();
         return ProtoStuffSerializerUtil.deserialize(result, targetClass);
     }
    public <T> List<T> getListCache(final String key, Class<T> targetClass) {
        byte[] result = redisTemplate.execute(new RedisCallback<byte[]>() {
            @Override
            public byte[] doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.get(key.getBytes());
            }
        });
        if (result == null) {
            return null;
        }
        return ProtoStuffSerializerUtil.deserializeList(result, targetClass);
    }

    /**
     * 精确删除key
     *
     * @param key
     */
    public void deleteCache(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 模糊删除key
     *
     * @param pattern
     */
    public void deleteCacheWithPattern(String pattern) {
        Set<String> keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }

    /**
     * 清空所有缓存
     */
    public void clearCache() {
        deleteCacheWithPattern(RedisCache.CAHCENAME+"|*");
    }
}

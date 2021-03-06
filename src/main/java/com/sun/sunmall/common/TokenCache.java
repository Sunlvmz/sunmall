package com.sun.sunmall.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by sun on 2017/5/12.
 */
//http://www.cnblogs.com/peida/p/Guava_Cache.html
    //http://www.cnblogs.com/parryyang/p/5777019.html
public class TokenCache {
    private static Logger logger = LoggerFactory.getLogger(TokenCache.class);
    //LLU算法
    private static LoadingCache<String,String> localCache = CacheBuilder.newBuilder().initialCapacity(1000).maximumSize(10000).expireAfterAccess(12, TimeUnit.HOURS)
            .build(new CacheLoader<String, String>() {
                //默认数据加载实现，当调用get取值的时候，如果KEY没有对应的值就调用这个方法加载
                @Override
                public String load(String s) throws Exception {
                    return "null";//用字符串防止出现NULLPOINTEREXCEPTION
                }
            });
    public   static  void setKey(String key,String value){
        localCache.put(key, value);
    }
    public static String getKey(String key){
        String value=null;
        try {
            value = localCache.get(key);
            if("null".equals(value)){
                return null;
            }
            return value;
        } catch (ExecutionException e) {
            logger.error("localCache get error",e);
        }
            return null;
    }
}

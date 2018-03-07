package com.sun.sunmall.service;

import com.sun.sunmall.common.SecKillEnum;

import java.util.Map;

/**
 * Created by sun on 2018/3/7.
 */
public interface ISeckillService {
    public SecKillEnum handleByPessLockInMySQL(Map<String, Object> paramMap);
    public SecKillEnum handleByPosiLockInMySQL(Map<String, Object> paramMap);
    public SecKillEnum handleByRedisWatch(Map<String, Object> paramMap);

}

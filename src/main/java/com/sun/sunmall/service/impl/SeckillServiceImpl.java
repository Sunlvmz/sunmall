package com.sun.sunmall.service.impl;

import ch.qos.logback.classic.Logger;
import com.sun.sunmall.common.SecKillEnum;
import com.sun.sunmall.common.SecKillException;
import com.sun.sunmall.mq.RabbitMQSender;
import com.sun.sunmall.pojo.Record;
import com.sun.sunmall.redis.RedisCacheHandle;
import com.sun.sunmall.service.ISeckillService;
import com.sun.sunmall.utils.ProtoStuffSerializerUtil;
import com.sun.sunmall.utils.SecKillUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.sun.sunmall.pojo.Product;
import com.sun.sunmall.pojo.User;
import com.sun.sunmall.dao.SeckillMapper;

@Service
public class SeckillServiceImpl implements ISeckillService {

    @Autowired
    private RedisCacheHandle redisCacheHandle;

    @Autowired
    private SeckillMapper seckillMapper;

    @Autowired
    private RabbitMQSender rabbitMQSender;
    private Logger log;

    /**
     * 利用MySQL的update行锁实现悲观锁
     *
     * @param paramMap
     * @return
     */
    @Transactional
    public SecKillEnum handleByPessLockInMySQL(Map<String, Object> paramMap) {
        Jedis jedis = redisCacheHandle.getJedis();
        Record record = null;
        Integer userId = (Integer) paramMap.get("userId");
        Integer productId = (Integer) paramMap.get("productId");
        User user = new User(userId);
        Product product = seckillMapper.getProductById(productId);
        String hasBoughtSetKey = SecKillUtils.getRedisHasBoughtSetKey(product.getName());

        //判断是否重复购买
        boolean isBuy = jedis.sismember(hasBoughtSetKey, Integer.valueOf(user.getId()).toString());
        if (isBuy) {
            //重复秒杀
            throw new SecKillException(SecKillEnum.REPEAT);
        }
        boolean secKillSuccess = seckillMapper.updatePessLockInMySQL(product);
        if (!secKillSuccess) {
            //库存不足
            throw new SecKillException(SecKillEnum.LOW_STOCKS);
        }

        //秒杀成功
        record = new Record( user, product, SecKillEnum.SUCCESS.getCode(), SecKillEnum.SUCCESS.getMessage(), new Date());
        log.info(record.toString());
        boolean insertFlag = seckillMapper.insertRecord(record);
        //插入record成功
        if (insertFlag) {
            long addResult = jedis.sadd(hasBoughtSetKey,  Integer.valueOf(user.getId()).toString());
            if (addResult > 0) {
                log.info("---------秒杀成功");
                return SecKillEnum.SUCCESS;
            } else {
                throw new SecKillException(SecKillEnum.REPEAT);
            }
        } else {
            throw new SecKillException(SecKillEnum.SYSTEM_EXCEPTION);
        }
    }

    /**
     * MySQL加字段version实现乐观锁
     *
     * @param paramMap
     * @return
     */
    @Transactional
    public SecKillEnum handleByPosiLockInMySQL(Map<String, Object> paramMap) {
        Jedis jedis = redisCacheHandle.getJedis();
        Record record = null;
        Integer userId = (Integer) paramMap.get("userId");
        Integer productId = (Integer) paramMap.get("productId");
        User user = new User(userId);
        Product product = seckillMapper.getProductById(productId);
        String hasBoughtSetKey = SecKillUtils.getRedisHasBoughtSetKey(product.getName());

        //判断是否重复购买
        boolean isBuy = jedis.sismember(hasBoughtSetKey,  Integer.valueOf(user.getId()).toString());
        if (isBuy) {
            //重复秒杀
            throw new SecKillException(SecKillEnum.REPEAT);
        }
        //库存减一
        int lastStock = product.getStock() - 1;
        if (lastStock >= 0) {
            product.setStock(lastStock);
            boolean secKillSuccess = seckillMapper.updatePosiLockInMySQL(product);
            if (!secKillSuccess) {
                //秒杀失败,version被修改
                throw new SecKillException(SecKillEnum.FAIL);
            }
        } else {
            //库存不足
            throw new SecKillException(SecKillEnum.LOW_STOCKS);
        }

        record = new Record( user, product, SecKillEnum.SUCCESS.getCode(), SecKillEnum.SUCCESS.getMessage(), new Date());
        log.info(record.toString());
        boolean insertFlag = seckillMapper.insertRecord(record);
        //插入record成功
        if (insertFlag) {
            long addResult = jedis.sadd(hasBoughtSetKey,  Integer.valueOf(user.getId()).toString());
            if (addResult > 0) {
                //秒杀成功
                return SecKillEnum.SUCCESS;
            } else {
                //重复秒杀
                log.info("---------重复秒杀");
                throw new SecKillException(SecKillEnum.REPEAT);
            }
        } else {
            //系统错误
            throw new SecKillException(SecKillEnum.SYSTEM_EXCEPTION);
        }
    }

    /**
     * redis的watch监控
     *
     * @param paramMap
     * @return
     */
    public SecKillEnum handleByRedisWatch(Map<String, Object> paramMap) {

        Jedis jedis = redisCacheHandle.getJedis();

        Record record = null;
        Integer userId = (Integer) paramMap.get("userId");
        Integer productId = (Integer) paramMap.get("productId");
        User user = new User(userId);

        String productName = jedis.get("product_" + productId);
        String hasBoughtSetKey = SecKillUtils.getRedisHasBoughtSetKey(productName);


        String productStockCacheKey = productName + "_stock";
        //watch开启监控
        jedis.watch(productStockCacheKey);

        //判断是否重复购买，注意这里高并发情形下并不安全
        boolean isBuy = jedis.sismember(hasBoughtSetKey,  Integer.valueOf(user.getId()).toString());
        if (isBuy) {
            //重复秒杀
            throw new SecKillException(SecKillEnum.REPEAT);
        }
        String stock = jedis.get(productStockCacheKey);
        if (Integer.parseInt(stock) <= 0) {
            //库存不足
            throw new SecKillException(SecKillEnum.LOW_STOCKS);
        }

        //开启Redis事务
        Transaction tx = jedis.multi();
        //库存减一
        tx.decrBy(productStockCacheKey, 1);
        //执行事务
        List<Object> resultList = tx.exec();

        if (resultList == null || resultList.isEmpty()) {
            jedis.unwatch();
            //watch监控被更改过----物品抢购失败;
            throw new SecKillException(SecKillEnum.FAIL);
        }

        //添加到已买队列
        long addResult = jedis.sadd(hasBoughtSetKey, Integer.valueOf(user.getId()).toString());
        if (addResult > 0) {
            Product product = new Product(productId);
            //秒杀成功
            record = new Record(user, product, SecKillEnum.SUCCESS.getCode(), SecKillEnum.SUCCESS.getMessage(), new Date());
            //添加record到rabbitMQ消息队列
            rabbitMQSender.send(ProtoStuffSerializerUtil.serialize(record));
            //返回秒杀成功
            return SecKillEnum.SUCCESS;
        } else {
            //重复秒杀
            //这里抛出RuntimeException异常，redis的decr操作并不会回滚，所以需要手动incr回去
            jedis.incrBy(productStockCacheKey, 1);
            throw new SecKillException(SecKillEnum.REPEAT);
        }
    }
}


package com.sun.sunmall.annotation;

import com.sun.sunmall.redis.RedisCache;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RedisAspect {

    @Autowired
    @Qualifier("redisCache")
    private RedisCache cache;

    @Pointcut("@annotation(com.sun.sunmall.annotation.Redis)") //@annotation用于匹配当前执行方法持有指定注解的方法；
    public void redisAspect() {
    }

    @Around("redisAspect()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        String applId = null;
        if (args != null && args.length > 0) {
            applId = String.valueOf(args[0]);
        }
//        RedisCache cache = new RedisCache();
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        String redisKey = targetName + methodName + applId;
        Class returnType = ((MethodSignature) joinPoint.getSignature()).getReturnType();
        Object objectFromRedis =  cache.getCache(redisKey, returnType); //todo 有resultType 怎么new一个对应对象  方案？返回ServeResoponse
        if (null != objectFromRedis) {
            System.out.printf("从redis查到了数据");
            return objectFromRedis;
        }
        System.out.println("没有从redis中查到数据...");

        //没有查到，那么查询数据库
        Object object = null;
        try {
            object = joinPoint.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        System.out.println("从数据库中查询的数据...");

        //后置：将数据库中查询的数据放到redis中
        System.out.println("调用把数据库查询的数据存储到redis中的方法...");

        cache.putCache(redisKey, object);

        //将查询到的数据返回
        return object;
    }

}




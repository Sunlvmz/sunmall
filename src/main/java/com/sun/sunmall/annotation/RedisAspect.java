package com.sun.sunmall.annotation;

import com.sun.sunmall.redis.RedisCache;
import com.sun.sunmall.utils.JointPointUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

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
//        Object[] args = joinPoint.getArgs();
//        Class[] classes = new Class[args.length];
//        String applId = null;
//        if (args != null && args.length > 0) {
//            applId = String.valueOf(args[0]);
//            for(int i=0;i<args.length;i++) {
//                classes[i] = args[i].getClass();
//            }
//        }
////        RedisCache cache = new RedisCache();
//        Class target = joinPoint.getTarget().getClass();
//        String targetName = target.getName();
//        String methodName = joinPoint.getSignature().getName();
//        String redisKey = targetName + methodName + applId;
//        Method method = target.getMethod(methodName, classes);
//        Type type = method.getGenericReturnType();
//        if (type instanceof ParameterizedType) { // 判断获取的类型是否是参数类型
//            System.out.println(type);
//            Type[] typesto = ((ParameterizedType) type).getActualTypeArguments();// 强制转型为带参数的泛型类型，
//            // getActualTypeArguments()方法获取类型中的实际类型，如map<String,Integer>中的
//            // String，integer因为可能是多个，所以使用数组
//        }
        String redisKey = JointPointUtil.getRedisKey(joinPoint);

//        Class returnType = ((MethodSignature) joinPoint.getSignature()).getReturnType();
        Class returnType = JointPointUtil.getTargetClass(joinPoint);
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




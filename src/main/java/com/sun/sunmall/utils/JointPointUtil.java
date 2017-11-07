package com.sun.sunmall.utils;

import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class JointPointUtil {
    public  static String getRedisKey(ProceedingJoinPoint joinPoint){
        Object[] args = joinPoint.getArgs();
        String applId = null;
        if (args != null && args.length > 0) {
            applId = String.valueOf(args[0]);
        }
        Class target = joinPoint.getTarget().getClass();
        String targetName = target.getName();
        String methodName = joinPoint.getSignature().getName();
        String redisKey = targetName + methodName + applId;
        return redisKey;
    }
    public static Class getTargetClass(ProceedingJoinPoint joinPoint){
        Object[] args = joinPoint.getArgs();
        Class[] classes = new Class[args.length];
        if (args != null && args.length > 0) {
            for(int i=0;i<args.length;i++) {
                classes[i] = args[i].getClass();
            }
        }
//        RedisCache cache = new RedisCache();
        Class target = joinPoint.getTarget().getClass();
        String methodName = joinPoint.getSignature().getName();
        Method method = null;
        try {
            if (args != null && args.length > 0)
                method = target.getMethod(methodName, classes);
            else
                method =target.getMethod(methodName, null);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        Type type = method.getGenericReturnType();
        if (type instanceof ParameterizedType) { // 判断获取的类型是否是参数类型
            System.out.println(type);
            Type[] typesto = ((ParameterizedType) type).getActualTypeArguments();// 强制转型为带参数的泛型类型，
            // getActualTypeArguments()方法获取类型中的实际类型，如map<String,Integer>中的
            // String，integer因为可能是多个，所以使用数组
            return (Class) typesto[0];
        }
       return (Class) type;
    }
}

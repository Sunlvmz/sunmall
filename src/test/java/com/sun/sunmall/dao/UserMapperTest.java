package com.sun.sunmall.dao;

import com.alibaba.fastjson.JSONArray;
import com.sun.sunmall.pojo.User;
import com.sun.sunmall.service.IUserService;
import com.sun.sunmall.utils.JsonUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by sun on 2017/5/12.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-*.xml"})
public class UserMapperTest {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private IUserService userService;
    @Test()
    public void checkUsername()  throws Exception {
       int resultCount=userMapper.checkUsername("admin");
        System.out.println(resultCount);
    }
    @Test()
    public void selectByPrimaryKey() throws Exception {
        User resultCount=userMapper.selectByPrimaryKey(1);
        System.out.println(resultCount.getUsername());
    }
//    @Test()
//    public void  testRedis(){
//        int id=22;
//        List<User> gagList= null;
//        try {
//            String resulthget = jedisClientService.hget("禁言表", id + "");
//            if (resulthget != null) {
//                //字符串转为list
//                System.out.println("有缓存啦啦啦！！！");
//                JSONArray array = JSONArray.parseArray(resulthget);
//                gagList = (List) array;
//            } else {
//                System.out.println("禁言表没查过");//没有查过就读取整个表的list出来
//                User user= userMapper.selectByPrimaryKey(id);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        try {
//            String cacheString = JsonUtils.objectToJson(gagList);//读取出来后就加载缓存进去，但是要转化给json
//            jedisClientService.hset("禁言表", id + "", cacheString);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}
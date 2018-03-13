package com.sun.sunmall.service.impl;

import com.sun.sunmall.common.ServerResponse;
import com.sun.sunmall.dao.UserMapper;
import com.sun.sunmall.pojo.User;
import com.sun.sunmall.service.IUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by sun on 2017/5/12.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-*.xml"})
public class UserServiceImplTest {

    @Autowired
    private UserServiceImpl userService;
//    @Test
//    public void getInformationRedis() throws Exception {
//        Integer id=22;
//        ServerResponse response= userService.getInformationRedis(id);
//        System.out.println(response.getData());
//    }
//    @Test
//    public void getUserList() throws Exception {
//        List<User> userList= userService.getUserList();
//    }
    @Test
    public void checkAnswer1() throws Exception {
          String username = "admin";
          String question = "新问题";
          String answer = "新答案";
          ServerResponse response= userService.checkAnswer(username,question,answer);
        System.out.println(response.getData());
    }

    @Test
    public void forgetSetPassword() throws Exception {
        String username = "admin";
        String question = "新问题";
        String answer = "新答案";
        ServerResponse response= userService.checkAnswer(username,question,answer);
        String passwordNew = "123";
        String forgetToken = (String) response.getData();
        ServerResponse newresponse = userService.forgetSetPassword(username,passwordNew, forgetToken);
    }


    @Test
    public void login() throws Exception {
        String username = "sunluhao";
        String password = "199536";
        userService.login(username,password);
    }

    @Test
    public void register() throws Exception {
    }

    @Test
    public void checkValid() throws Exception {

    }

    @Test
    public void getUserInfo() throws Exception {

    }

    @Test
    public void logout() throws Exception {

    }

    @Test
    public void selectQuestion() throws Exception {

    }

    @Test
    public void checkAnswer() throws Exception {

    }

}
package com.sun.sunmall.service;

import com.sun.sunmall.common.ServerResponse;
import com.sun.sunmall.pojo.User;

import javax.servlet.http.HttpSession;

/**
 * Created by sun on 2017/5/10.
 */

public interface IUserService {
      ServerResponse<User> login(String username,String password);
      ServerResponse<String> register(User user);
      ServerResponse<String> checkValid(String str,String type);
      ServerResponse<User> getUserInfo(HttpSession session);
      ServerResponse<User> logout(HttpSession session);
      ServerResponse<String> selectQuestion(String username);
      ServerResponse<String> checkAnswer(String username,String question,String answer);
      ServerResponse<String> forgetSetPassword(String username,String passwordNew,String forgetToken);
      ServerResponse<String> resetPassword(HttpSession session,String passwordOld,String passwordNew);
      ServerResponse<User> updateInformation(HttpSession session,User newuser);
      ServerResponse<User> getInformation(Integer userId);
//      ServerResponse<User> getInformationRedis(Integer userId);
      ServerResponse checkAdminRole(User user);
      User getUser(Integer id);
}

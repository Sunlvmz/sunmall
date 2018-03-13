package com.sun.sunmall.service.impl;

import com.sun.sunmall.annotation.Redis;
import com.sun.sunmall.common.Const;
import com.sun.sunmall.common.ServerResponse;
import com.sun.sunmall.common.TokenCache;
import com.sun.sunmall.dao.UserMapper;
import com.sun.sunmall.pojo.User;

import com.sun.sunmall.redis.RedisCache;
import com.sun.sunmall.service.IUserService;
import com.sun.sunmall.utils.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.UUID;

/**
 * Created by sun on 2017/5/10.
 */
@Service("iUserService")
public class UserServiceImpl implements IUserService{

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisCache redisCache;
    @Override
    public ServerResponse<User> login(String username, String password) {
       int resultCount = userMapper.checkUsername(username);
       if(resultCount == 0){
           return ServerResponse.createByErrorMessage("用户名不存在");
       }
       //TODO 密码MD5加密
        String md5Password = MD5Util.MD5EncodeUtf8(password);

        User user= userMapper.selectLogin(username,md5Password);
       if(user == null){
           return ServerResponse.createByErrorMessage("密码错误");
       }
       user.setPassword(StringUtils.EMPTY);//为了安全起见。
       return  ServerResponse.createBySuccess("登陆成功",user);
    }

    @Override
    public ServerResponse<String> register(User user) {
         ServerResponse validResponse = this.checkValid(user.getUsername(),Const.USERNAME);
         if(!validResponse.isSuccess()){
             return  validResponse;
         }

         validResponse = this.checkValid(user.getEmail(),Const.EMAIL);
        if(!validResponse.isSuccess()){
            return  validResponse;
        }

        user.setRole(Const.Role.ROLE_CUSTOMER);
        //MD5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
       int resultCount=userMapper.insert(user);
        if(resultCount == 0){
            return ServerResponse.createByErrorMessage("注册失败");
        }
        return ServerResponse.createBySuccessMessage("注册成功");

    }

    @Override
    public ServerResponse<String> checkValid(String str, String type) {
        if (StringUtils.isNotBlank(type)) {//isNotBlank与isNotEmpty区别
            if (Const.USERNAME.equals(type)) {
                int resultCount = userMapper.checkUsername(str);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMessage("用户名已存在");
                }
            }
                if (Const.EMAIL.equals(type)) {
                   int resultCount = userMapper.checkEmail(str);
                    if (resultCount > 0) {
                        return ServerResponse.createByErrorMessage("email已存在");}
                }
            }
            else {
                return ServerResponse.createByErrorMessage("参数错误");
                }
            return ServerResponse.createBySuccessMessage("可以注册");
        }
    @Override
    public ServerResponse<User> getUserInfo(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user!= null){
            return ServerResponse.createBySuccess(user);
        }
        return  ServerResponse.createByErrorMessage("用户未登录");
    }

    @Override
    public ServerResponse<User> logout(HttpSession session) {
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }

    @Override
    public ServerResponse<String> selectQuestion(String username) {
        ServerResponse validResponse = this.checkValid(username,Const.USERNAME);
        if(validResponse.isSuccess()){
            return  ServerResponse.createByErrorMessage("用户不存在");
        }
        String question = userMapper.selectQuestionByUsername(username);
        if(StringUtils.isNotBlank(question)){
            return ServerResponse.createBySuccess(question);
        }
        return ServerResponse.createByErrorMessage("找回密码问题为空");
    }

    @Override
    public ServerResponse<String> checkAnswer(String username, String question, String answer) {//需要question的token
        int resultCount=userMapper.checkAnswer(username, question, answer);
        if(resultCount>0){
            //问题 答案与用户相匹配
            String forgetToken= UUID.randomUUID().toString();
//            TokenCache.setKey(Const.TOKEN_PREFIX+username,forgetToken);
            redisCache.putCache(Const.TOKEN_PREFIX+username,forgetToken);
            return ServerResponse.createBySuccess(forgetToken);
        }
        return ServerResponse.createByErrorMessage("答案错误");
    }
    @Override
    public ServerResponse<String> forgetSetPassword(String username, String passwordNew, String forgetToken) {
        if(!StringUtils.isNotBlank(forgetToken)){
            return  ServerResponse.createByErrorMessage("参数错误，重新传入Token");
        }
        //校验Username。否则只有token_，没有变量，不安全。
//        String token = TokenCache.getKey(Const.TOKEN_PREFIX+username);
        String token = redisCache.getCache(Const.TOKEN_PREFIX + username,String.class);
        if(!StringUtils.isNotBlank(token)){//注意前面的！取非操作；或采用isBlank方法
            return ServerResponse.createByErrorMessage("token无效或过期");
        }

        if(StringUtils.equals(forgetToken,token)){
            String md5password = MD5Util.MD5EncodeUtf8(passwordNew);
            int resultCount =userMapper.updatePasswordByUsername(username, md5password);
            if (resultCount>0){
                return ServerResponse.createBySuccessMessage("修改密码成功");
            }else {
                return ServerResponse.createByErrorMessage("token错误，重置密码错误");
            }

        }
        return ServerResponse.createByErrorMessage("修改密码失败");
    }

    @Override
    public ServerResponse<String> resetPassword(HttpSession session, String passwordOld, String passwordNew) {
       User user = (User) session.getAttribute(Const.CURRENT_USER);
       if(user == null){
           return  ServerResponse.createByErrorMessage("用户未登录");
       }
       else{
          int resultCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld),user.getId());
          if(resultCount==0){
              return ServerResponse.createByErrorMessage("旧密码错误");
          }
       }
       user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
       int  updateCount = userMapper.updateByPrimaryKeySelective(user);
       if(updateCount>0){
           return ServerResponse.createBySuccessMessage("密码更新成功");
       }
       return ServerResponse.createByErrorMessage("密码更新失败");
    }

    @Override
    public ServerResponse<User> updateInformation(HttpSession session, User newuser) {
        User currentUser = (User)session.getAttribute(Const.CURRENT_USER);
        if(currentUser==null){
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        newuser.setId(currentUser.getId());//防止越权查询
        newuser.setUsername(currentUser.getUsername());//防止越权查询
        int resultCount =userMapper.checkEmailByUserId(newuser.getEmail(), newuser.getId());
        if(resultCount!=0){
            return ServerResponse.createByErrorMessage("Email已存在");
        }
        User updateUser = new User();
        updateUser.setId(newuser.getId());
        updateUser.setUsername(newuser.getUsername());
        updateUser.setEmail(newuser.getEmail());
        updateUser.setPhone(newuser.getPhone());
        updateUser.setQuestion(newuser.getQuestion());
        updateUser.setAnswer(newuser.getAnswer());
        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if(updateCount>0){
            return ServerResponse.createBySuccess("更新用户信息成功",updateUser);
        }
        return ServerResponse.createByErrorMessage("更新用户信息失败");
    }

    @Override
    public ServerResponse<User> getInformation(Integer userId){
        User user = userMapper.selectByPrimaryKey(userId);
        if(user == null){
            return ServerResponse.createByErrorMessage("找不到当前用户");
        }
        user.setPassword(org.apache.commons.lang3.StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);

    }
//    @Override
//    public ServerResponse<User> getInformationRedis(Integer userId){
//        User user =null;
//        try {
//            String resulthget = jedisClientService.hget("禁言表", userId + "");
//            if (resulthget != null) {
//                //字符串转为list
//                System.out.println("有缓存！！！");
//                System.out.println(resulthget);
//            } else {
//                System.out.println("禁言表没查过");
//                user = userMapper.selectByPrimaryKey(userId);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        if(user == null){
//            return ServerResponse.createByErrorMessage("找不到当前用户");
//        }
//        user.setPassword(org.apache.commons.lang3.StringUtils.EMPTY);
//        try {
//            String cacheString = JsonUtils.objectToJson(user);
//            jedisClientService.hset("禁言表", userId + "", cacheString);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return ServerResponse.createBySuccess(user);
//
//    }


    //backend

    /**
     * 校验是否是管理员
     * @param user
     * @return
     */
    public ServerResponse checkAdminRole(User user){
        if(user != null && user.getRole().intValue() == Const.Role.ROLE_ADMIN){
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }

//    @Autowired
//    private RedisCache cache;
//    public List<User> getUserList() {
//        String cache_key= RedisCache.CAHCENAME;
//        //先去缓存中取
//        List<User> result_cache=cache.getListCache(cache_key, User.class);
//        if(result_cache==null){
//            //缓存中没有再去数据库取，并插入缓存（缓存时间为60秒）
//            result_cache = userMapper.listUser();
//            cache.putListCacheWithExpireTime(cache_key, result_cache, RedisCache.CAHCETIME);
//            System.out.printf("put in cache");
//        }else{
//            System.out.printf("get from cache");
//        }
//        return result_cache;
//    }

}

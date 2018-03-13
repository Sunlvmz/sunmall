package com.sun.sunmall.controller.portal;

import com.sun.sunmall.common.Const;
import com.sun.sunmall.common.ResponseCode;
import com.sun.sunmall.common.ServerResponse;
import com.sun.sunmall.pojo.User;
//import com.sun.sunmall.redis.RedisCache;
import com.sun.sunmall.redis.ShardedRedisCacheUtils;
import com.sun.sunmall.service.IUserService;
import com.sun.sunmall.utils.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by sun on 2017/5/10.
 */
@Controller
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private ShardedRedisCacheUtils redisCache;

    @RequestMapping(value = "login.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session, HttpServletResponse httpServletResponse){
        ServerResponse<User> response = iUserService.login(username, password);
        if(response.isSuccess()){
            CookieUtil.writeLoginToken(httpServletResponse,session.getId()); //将sessionId写入到Cookie中
            redisCache.putCacheWithExpireTime(session.getId(),response.getData(),60*30); //将sessionid存入redis,这样就可以通过sessionId将user信息与cookie连接起来
//            session.setAttribute(Const.CURRENT_USER,response.getData()); //之前直接存入
        }
        return response;
    }
    @RequestMapping(value = "logout.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> logout(HttpServletRequest request,HttpServletResponse response) {
        String token = CookieUtil.readLoginToken(request);
        if (StringUtils.isBlank(token)) {
            return ServerResponse.createByErrorMessage("未登录");
        }
        CookieUtil.delLoginToken(request,response);
        redisCache.deleteCache(token);
        return ServerResponse.createBySuccess();
//        return iUserService.logout(session);
    }
    @RequestMapping(value = "register.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(User user){
           return iUserService.register(user);
    }
    //做一个实时反馈。即下个input框前 反馈前一个输入是否成功或合法
    @RequestMapping(value = "check_valid.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkValid(String str,String type){
        return iUserService.checkValid(str, type);
    }
    @RequestMapping(value = "get_user_info.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession session){
        return iUserService.getUserInfo(session);
    }
    @RequestMapping(value = "forget_get_question.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetGetQuestion(String username){
        return iUserService.selectQuestion(username);
    }
    @RequestMapping(value = "forget_check_answer.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer(String username, String question, String answer){
        return iUserService.checkAnswer(username, question, answer);
    }
    @RequestMapping(value = "forget_set_password.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetSetPassword(String username, String passwordNew, String forgetToken){
        return iUserService.forgetSetPassword(username, passwordNew, forgetToken);
    }
    @RequestMapping(value = "reset_password.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPassword(HttpSession session, String passwordOld, String passwordNew){
        return iUserService.resetPassword(session, passwordOld, passwordNew);
    }
    @RequestMapping(value = "update_information.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> updateInformation(HttpSession session, User newuser){
        ServerResponse response= iUserService.updateInformation(session, newuser);
        if(response.isSuccess()){
            session.setAttribute(Const.CURRENT_USER,response.getData());
        }
        return response;
    }



    @RequestMapping(value = "get_information.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> get_information( HttpServletRequest httpServletRequest){
//        User currentUser = (User)session.getAttribute(Const.CURRENT_USER);
        String token = CookieUtil.readLoginToken(httpServletRequest);
        if(token == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"未登录,需要强制登录status=10");
        }
        User user = redisCache.getCache(token, User.class);
        return iUserService.getInformation(user.getId());
    }


    @RequestMapping(value = "get_information_redis.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> get_information_id(int id){
        return iUserService.getInformation(id);
    }

    ServerResponse c = ServerResponse.createByError();
}

package com.sun.sunmall.controller.common;



import com.sun.sunmall.common.Const;
import com.sun.sunmall.pojo.User;
import com.sun.sunmall.redis.RedisCache;
import com.sun.sunmall.utils.CookieUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by sun
 */
public class SessionExpireFilter implements Filter {
    @Autowired
    private RedisCache redisCache;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest)servletRequest;

        String loginToken = CookieUtil.readLoginToken(httpServletRequest);

        if(StringUtils.isNotEmpty(loginToken)){
            //判断logintoken是否为空或者""；
            //如果不为空的话，符合条件，继续拿user信息

            User user = redisCache.getCache(loginToken, User.class);
            if(user != null){
                //如果user不为空，则重置session的时间，即调用expire命令
                redisCache.updateExpireTime(loginToken, Const.REDIS_SESSION_EXPIRETIME);
            }
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }


    @Override
    public void destroy() {

    }
}

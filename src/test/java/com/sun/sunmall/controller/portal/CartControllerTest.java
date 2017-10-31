package com.sun.sunmall.controller.portal;

import com.sun.sunmall.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import java.util.Enumeration;

import static org.junit.Assert.*;

/**
 * Created by sun on 2017/5/22.
 */
@RunWith(SpringJUnit4ClassRunner.class)

@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class CartControllerTest {
    @Autowired
    private CartController cartController;
    @Test
    public void addProductToCart() throws Exception {
        User user = new User();
    }

}
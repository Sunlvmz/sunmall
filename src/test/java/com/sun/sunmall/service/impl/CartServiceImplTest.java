package com.sun.sunmall.service.impl;

import com.sun.sunmall.pojo.Product;
import com.sun.sunmall.vo.CartProductVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by sun on 2017/5/22.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class CartServiceImplTest {
    @Autowired
    private CartServiceImpl cartService;
    @Test
    public void addProductToCart() throws Exception {
      Integer productId = 26;
      Integer userId = 22;
      Integer count =10;
        cartService.addProductToCart(productId, userId, count);
    }

    @Test
    public void updateCart() throws Exception {

    }

    @Test
    public void deleteProductInCart() throws Exception {

    }

    @Test
    public void listCart() throws Exception {
        List<CartProductVO> productList=cartService.listCart(22).getData().getCartProductVOList();
        for (CartProductVO cartProductVO : productList) {
            System.out.println(cartProductVO.getProductName());
        }
    }

    @Test
    public void selectOrUnselect() throws Exception {

    }

    @Test
    public void getCartProductCount() throws Exception {

    }

}
package com.sun.sunmall.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by sun on 2017/5/23.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class CartMapperTest {
    @Autowired
    private CartMapper cartMapper;
    @Test
    public void deleteByPrimaryKey() throws Exception {

    }

    @Test
    public void insert() throws Exception {

    }

    @Test
    public void insertSelective() throws Exception {

    }

    @Test
    public void selectByPrimaryKey() throws Exception {

    }

    @Test
    public void updateByPrimaryKeySelective() throws Exception {

    }

    @Test
    public void updateByPrimaryKey() throws Exception {

    }

    @Test
    public void selectByProductIdUserId() throws Exception {

    }

    @Test
    public void selectCartByUserId() throws Exception {

    }

    @Test
    public void selectCartProductCheckedStatus() throws Exception {

    }

    @Test
    public void deleteProductByUserIdProductIds() throws Exception {

    }

    @Test
    public void checkedOrUncheckedProduct() throws Exception {
        cartMapper.checkedOrUncheckedProduct(0, 22, 28);
    }

    @Test
    public void selectCartProductCount() throws Exception {

    }

}
package com.sun.sunmall.service.impl;

import com.github.pagehelper.PageInfo;
import com.sun.sunmall.common.ServerResponse;
import com.sun.sunmall.dao.ProductMapper;
import com.sun.sunmall.service.IProductService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by sun on 2017/5/18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class ProductServiceImplTest {
    @Autowired
    private ProductServiceImpl productService;
    @Autowired
    private ProductMapper productMapper;
    @Test
    public void searchPortalProduct() throws Exception {
        ServerResponse<PageInfo> result =  productService.searchPortalProduct("美的",null,1,5,"price_desc");
        //没有搜到是因为status设置为了2！！
        System.out.println(result);
    }

}
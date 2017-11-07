package com.sun.sunmall.dao;

import com.google.common.collect.Lists;
import com.sun.sunmall.pojo.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by sun on 2017/5/18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-*.xml"})
public class ProductMapperTest {
    @Autowired
    private  ProductMapper productMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Test
    public void selectByKeywordAndCategoryIds() throws Exception {
               String keyword = "%美的%";
               //Integer categoryId=100002;
        List<Integer> categoryIdList = Lists.newArrayList();
        // categoryIdList.add(100002);
        List<Product> productList= productMapper.selectByKeywordAndCategoryIds(keyword,null);
        for (Product product : productList) {
            System.out.println(product.getName());
        }
    }
    @Test
    public void selectByProductInfo() throws Exception{
        String keyword = "%美的%";
        //Integer categoryId=100002;
        List<Integer> categoryIdList = Lists.newArrayList();
        // categoryIdList.add(100002);
        List<Product> productList= productMapper.selectByProductInfo(keyword,null);
        for (Product product : productList) {
            System.out.println(product.getName());
        }
    }

}
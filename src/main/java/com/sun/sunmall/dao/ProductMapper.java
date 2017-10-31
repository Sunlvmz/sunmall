package com.sun.sunmall.dao;

import com.sun.sunmall.pojo.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    List<Product> selectProductList();

    List<Product> selectByProductInfo(@Param("productName") String productName, @Param("productId") Integer productId);

    List<Product> selectByKeywordAndCategoryIds(@Param("keyword")String keyword,@Param("categoryIdList") List<Integer> categoryIdList);
}
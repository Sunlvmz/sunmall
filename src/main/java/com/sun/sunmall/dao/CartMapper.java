package com.sun.sunmall.dao;

import com.sun.sunmall.pojo.Cart;
import com.sun.sunmall.pojo.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    Cart selectByProductIdUserId(@Param("productId") Integer productId,@Param("userId")  Integer userId);

    List<Cart> selectCartByUserId(Integer userId);

    int selectCartProductCheckedStatus(Integer userId);

    int deleteProductByUserIdProductIds(@Param("productIds") List<String> productIds, @Param("userId")  Integer userId);

    int checkedOrUncheckedProduct(@Param("checked") Integer checked, @Param("userId") Integer userId,@Param("productId") Integer productId);

    int selectCartProductCount(Integer userId);
}
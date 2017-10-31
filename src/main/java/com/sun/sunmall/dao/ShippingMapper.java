package com.sun.sunmall.dao;

import com.sun.sunmall.pojo.Shipping;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShippingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Shipping record);

    int insertSelective(Shipping record);

    Shipping selectByPrimaryKey(Integer id);

    Shipping selectByShippingIdUserId(@Param("shippingId") Integer shippingId, @Param("userId") Integer userId);

    int updateByPrimaryKeySelective(Shipping record);

    int updateByPrimaryKey(Shipping record);

    int deleteByShippingId(@Param("userId") Integer userId, @Param("shippingId") Integer shippingId);

    int updateByShippingIdUserId(Shipping shipping);

    List<Shipping> selectAllShipping(Integer userId);
}
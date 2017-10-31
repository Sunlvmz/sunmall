package com.sun.sunmall.service;

import com.github.pagehelper.PageInfo;
import com.sun.sunmall.common.ServerResponse;
import com.sun.sunmall.pojo.Shipping;

import java.util.List;

/**
 * Created by sun on 2017/5/23.
 */
public interface IShippingService {
    ServerResponse addShipping(Integer userId,Shipping shipping);

    ServerResponse<String> deleteShipping(Integer userId, Integer shippingId);

    ServerResponse<String> updateShipping(Integer userId, Shipping shipping);

    ServerResponse<Shipping> selectShipping(Integer userId, Integer shippingId);

    ServerResponse<PageInfo> listShippings(Integer pageNum,Integer pageSize,Integer userId);
}

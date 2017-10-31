package com.sun.sunmall.service;

import com.sun.sunmall.common.ServerResponse;
import com.sun.sunmall.vo.CartVO;

/**
 * Created by sun on 2017/5/22.
 */
public interface ICartService {
    ServerResponse<CartVO> addProductToCart(Integer productId, Integer userId, Integer count);
    ServerResponse<CartVO> updateCart(Integer productId, Integer userId, Integer count);
    ServerResponse<CartVO> deleteProductInCart(Integer userId,String productIds);

    ServerResponse<CartVO> listCart(Integer id);
    ServerResponse<CartVO> selectOrUnselect(Integer id,Integer checked,Integer productId);

    ServerResponse<Integer> getCartProductCount(Integer userId);
}

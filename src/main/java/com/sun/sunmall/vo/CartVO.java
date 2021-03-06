package com.sun.sunmall.vo;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by sun on 2017/5/22.
 */
public class CartVO {
    private  List<CartProductVO> cartProductVOList;
    private BigDecimal cartTotalPrice;
    private Boolean allChecked;
    private String imageHost;

    public List<CartProductVO> getCartProductVOList() {
        return cartProductVOList;
    }

    public void setCartProductVOList(List<CartProductVO> cartProductVOList) {
        this.cartProductVOList = cartProductVOList;
    }

    public BigDecimal getCartTotalPrice() {
        return cartTotalPrice;
    }

    public void setCartTotalPrice(BigDecimal cartTotalPrice) {
        this.cartTotalPrice = cartTotalPrice;
    }

    public Boolean getAllChecked() {
        return allChecked;
    }

    public void setAllChecked(Boolean allChecked) {
        this.allChecked = allChecked;
    }

    public String getImageHost() {
        return imageHost;
    }

    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }
}

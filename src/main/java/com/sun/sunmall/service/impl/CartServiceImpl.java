package com.sun.sunmall.service.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.sun.sunmall.common.Const;
import com.sun.sunmall.common.ResponseCode;
import com.sun.sunmall.common.ServerResponse;
import com.sun.sunmall.dao.CartMapper;
import com.sun.sunmall.dao.ProductMapper;
import com.sun.sunmall.pojo.Cart;
import com.sun.sunmall.pojo.Product;
import com.sun.sunmall.service.ICartService;
import com.sun.sunmall.utils.BigDecimalUtil;
import com.sun.sunmall.utils.PropertiesUtil;
import com.sun.sunmall.vo.CartProductVO;
import com.sun.sunmall.vo.CartVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Created by sun on 2017/5/22.
 */
@Service("iCartService")
public class CartServiceImpl implements ICartService {
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;
    @Override
    public ServerResponse<CartVO> addProductToCart(Integer productId, Integer userId, Integer count) {
        if (productId == null || count == null) {
           return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Cart cart = cartMapper.selectByProductIdUserId(productId, userId);
        if (cart == null) {//产品没在当前购物车里，需新建购物车
            Cart newCart = new Cart();
            newCart.setUserId(userId);
            newCart.setProductId(productId);
            newCart.setQuantity(count);
            newCart.setChecked(Const.Cart.CHECKED);
            cartMapper.insert(newCart);
        } else {//产品在当前购物车 数量相加即可
            count = cart.getQuantity() + count;
            cart.setQuantity(count);
            cartMapper.updateByPrimaryKeySelective(cart);
        }
        CartVO cartVO = this.getCartVOLimit(userId);
        return ServerResponse.createBySuccess(cartVO);
    }

    @Override
    public ServerResponse<CartVO> updateCart(Integer productId, Integer userId, Integer count) {
        if (productId == null || count == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Cart cart = cartMapper.selectByProductIdUserId(productId, userId);
        if (cart != null) {
            cart.setQuantity(count);
        }
        cartMapper.updateByPrimaryKeySelective(cart);
        CartVO cartVO =this.getCartVOLimit(userId);
        return ServerResponse.createBySuccess(cartVO);
    }

    @Override
    public ServerResponse<CartVO> deleteProductInCart(Integer userId,String productIds) {
//        if (productIds == null) {
//            return
//        }
        List<String> productList = Splitter.on(",").splitToList(productIds);
        if (CollectionUtils.isEmpty(productList)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        cartMapper.deleteProductByUserIdProductIds(productList,userId);
        CartVO cartVO = this.getCartVOLimit(userId);
        return ServerResponse.createBySuccess(cartVO);
    }

    @Override
    public ServerResponse<CartVO> listCart(Integer userId) {
        CartVO cartVO = this.getCartVOLimit(userId);
        return ServerResponse.createBySuccess(cartVO);
    }

    @Override
    public ServerResponse<CartVO> selectOrUnselect(Integer userId ,Integer checked,Integer productId) {
        cartMapper.checkedOrUncheckedProduct(checked,userId,productId);
        return this.listCart(userId);
    }

    @Override
    public ServerResponse<Integer> getCartProductCount(Integer userId) {
        if (userId == null) {
            return ServerResponse.createBySuccess(0);
        }
        return ServerResponse.createBySuccess(cartMapper.selectCartProductCount(userId));
    }


    private CartVO getCartVOLimit(Integer userId) {
        CartVO cartVO = new CartVO();
        List<Cart> cartList = cartMapper.selectCartByUserId(userId);
        List<CartProductVO> cartProductVOList =Lists.newArrayList();
        BigDecimal cartTotalPrice = new BigDecimal("0");//商业运算中double float 丢失精度？ BigDecimal 的使用？

        for (Cart cartItem : cartList) {
            CartProductVO cartProductVO = assembleCartProductVO(cartItem);
            if (cartItem.getChecked() == Const.Cart.CHECKED) {
                //若已勾选，则将产品总价加到购物车总价
                cartTotalPrice=BigDecimalUtil.add(cartTotalPrice.doubleValue(), cartProductVO.getProductTotalPrice().doubleValue());
            }
            cartProductVOList.add(cartProductVO);
        }

        cartVO.setCartTotalPrice(cartTotalPrice);
        cartVO.setCartProductVOList(cartProductVOList);
        cartVO.setAllChecked(getAllCheckedStatus(userId));
        cartVO.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
        return cartVO;
    }

    private CartProductVO assembleCartProductVO(Cart cartItem) {
        CartProductVO cartProductVO = new CartProductVO();
        cartProductVO.setId(cartItem.getId());
        cartProductVO.setProductId(cartItem.getProductId());
        cartProductVO.setUserId(cartItem.getId());

        Product product = productMapper.selectByPrimaryKey(cartItem.getProductId());
        if (product != null) {
            cartProductVO.setProductMainImage(product.getMainImage());
            cartProductVO.setProductName(product.getName());
            cartProductVO.setProductPrice(product.getPrice());
            cartProductVO.setProductSubtitle(product.getSubtitle());
            cartProductVO.setProductStock(product.getStock());
            cartProductVO.setProductStatus(product.getStatus());

            int buyLimitCount = 0;
            if (product.getStock() > cartItem.getQuantity()) {
                buyLimitCount = cartItem.getQuantity();
                cartProductVO.setLimitQuantity(Const.Cart.LIMIT_NUM_SUCCESS);
            } else {
                buyLimitCount = product.getStock();
                cartProductVO.setLimitQuantity(Const.Cart.LIMIT_NUM_FAIL);
                //更新购物车中数量
                Cart updatedCart = new Cart();
                updatedCart.setId(cartItem.getId());
                updatedCart.setQuantity(buyLimitCount);
                cartMapper.updateByPrimaryKeySelective(updatedCart);
            }
            cartProductVO.setQuantity(buyLimitCount);
            cartProductVO.setProductTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(),cartProductVO.getQuantity()));
            cartProductVO.setProductChecked(cartItem.getChecked());
        }
        return cartProductVO;
    }

    private boolean getAllCheckedStatus(Integer userId) {
        if (userId == null) {
            return false;
        }
        return cartMapper.selectCartProductCheckedStatus(userId)==0;//取了逻辑逆
    }






}

package com.sun.sunmall.controller.portal;

import com.sun.sunmall.common.Const;
import com.sun.sunmall.common.ResponseCode;
import com.sun.sunmall.common.ServerResponse;
import com.sun.sunmall.pojo.User;
import com.sun.sunmall.service.ICartService;
import com.sun.sunmall.vo.CartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by sun on 2017/5/22.
 */
@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private ICartService iCartService;


    @RequestMapping("/add.do")
    @ResponseBody
    public ServerResponse<CartVO> addProductToCart(HttpSession session,Integer productId,Integer count){//todo 在传参数时 productId 的后面多加了个空格 引发的血案 productId =26
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        System.out.println("测试参数传递");
        System.out.println(productId);
        return iCartService.addProductToCart(productId, user.getId(),count);

    }

    @RequestMapping("/update.do")
    @ResponseBody
    public ServerResponse<CartVO> updateCart(HttpSession session, Integer productId, Integer count){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.updateCart(productId, user.getId(), count);
    }

    @RequestMapping("/delete.do")
    @ResponseBody
    public ServerResponse<CartVO> updateCart(HttpSession session, String productIds){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.deleteProductInCart(user.getId(), productIds);
    }

    @RequestMapping("/list.do")
    @ResponseBody
    public ServerResponse<CartVO> listCart(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.listCart(user.getId());
    }

    //全选
    @RequestMapping("/select_all.do")
    @ResponseBody
    public ServerResponse<CartVO> selectAll(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.selectOrUnselect(user.getId(),Const.Cart.CHECKED,null);
    }
    //全不选
    @RequestMapping("/unSelect_all.do")
    @ResponseBody
    public ServerResponse<CartVO> unSelectAll(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.selectOrUnselect(user.getId(),Const.Cart.UNCHECKED,null);
    }

    //单选
    @RequestMapping("/select_one.do")
    @ResponseBody
    public ServerResponse<CartVO> selectOne(HttpSession session,Integer productId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.selectOrUnselect(user.getId(),Const.Cart.CHECKED,productId);
    }
    //单不选
    @RequestMapping("/unSelect_one.do")
    @ResponseBody
    public ServerResponse<CartVO> unSelectOne(HttpSession session,Integer productId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.selectOrUnselect(user.getId(),Const.Cart.UNCHECKED,productId);
    }

    //获取购物车产品总数
    @RequestMapping("/get_Cart_Product_Count.do")
    @ResponseBody
    public ServerResponse<Integer> getCount(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createBySuccess(0);
        }
        return iCartService.getCartProductCount(user.getId());
    }





}

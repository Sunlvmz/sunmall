package com.sun.sunmall.controller.backend;

import com.sun.sunmall.common.Const;
import com.sun.sunmall.common.ResponseCode;
import com.sun.sunmall.common.ServerResponse;
import com.sun.sunmall.pojo.User;
import com.sun.sunmall.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.sun.sunmall.service.IUserService;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by sun on 2017/5/15.
 */
@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private ICategoryService iCategoryService;

    @RequestMapping(value = "add_category.do")
    @ResponseBody
    //采用RESTful风格时，value="category/{parentId}/add"  方法中注解用@PathVariable代替@RequestParam
    public ServerResponse addCategory(String categoryName, @RequestParam(value = "parentId",defaultValue = "0")Integer parentId){
//        User user = (User) session.getAttribute(Const.CURRENT_USER);
//        if(user==null){
//            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请先登录");
//        }
//        if(iUserService.checkAdminRole(user).isSuccess()){
//           return iCategoryService.addCategory(categoryName,parentId);
//        }
//        else {return ServerResponse.createByErrorMessage("非管理员，无法操作");}
        //全部通过权限校验。不在Controller里进行判断
        return iCategoryService.addCategory(categoryName,parentId);
    }

    @RequestMapping(value = "set_category_name.do")
    @ResponseBody
    public ServerResponse setCategoryName(HttpSession session, String categoryName,Integer categoryId){
//        User user = (User) session.getAttribute(Const.CURRENT_USER);
//        if(user==null){
//            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请先登录");
//        }
//        if(iUserService.checkAdminRole(user).isSuccess()){
//            return iCategoryService.updateCategoryName(categoryName,categoryId);
//        }
//        else {return ServerResponse.createByErrorMessage("非管理员，无法操作");}
        return iCategoryService.updateCategoryName(categoryName,categoryId);
    }

    @RequestMapping(value = "get_category_parallel.do")
    @ResponseBody
    public ServerResponse getChildrenParallelCategory(HttpSession session,@RequestParam(value = "parentId",defaultValue = "0")Integer parentId){
//        User user = (User) session.getAttribute(Const.CURRENT_USER);
//        if(user==null){
//            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请先登录");
//        }
//        if(iUserService.checkAdminRole(user).isSuccess()){
//            return iCategoryService.getChildrenParallelCategory(parentId);
//        }
//        else {return ServerResponse.createByErrorMessage("非管理员，无法操作");}
        return iCategoryService.getChildrenParallelCategory(parentId);
    }

    @RequestMapping(value = "get_category_deep.do")
    @ResponseBody
    public ServerResponse getCategoryAndDeepChildrenCategory(HttpSession session,@RequestParam(value = "categoryId",defaultValue = "0")Integer categoryId){
//        User user = (User) session.getAttribute(Const.CURRENT_USER);
//        if(user==null){
//            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请先登录");
//        }
//        if(iUserService.checkAdminRole(user).isSuccess()){
//            //递归查询 0-》10000-》10005->10012
//            return iCategoryService.getCategoryAndDeepChildrenCategory(categoryId);
//        }
//        else {return ServerResponse.createByErrorMessage("非管理员，无法操作");}
        return iCategoryService.getCategoryAndDeepChildrenCategory(categoryId);
    }


}

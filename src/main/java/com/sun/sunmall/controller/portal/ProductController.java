package com.sun.sunmall.controller.portal;

import com.github.pagehelper.PageInfo;
import com.sun.sunmall.common.ServerResponse;
import com.sun.sunmall.service.IProductService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by sun on 2017/5/18.
 */
@Controller
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private IProductService iProductService;

    @RequestMapping(value = "/{productId}",method = RequestMethod.GET)
    @ResponseBody//  todo 不加这个 用RESTLET 会返回一个找不到 .jsp 的错误
    public ServerResponse getProductDetail(@RequestParam("productId") Integer productId){
        return iProductService.getPortalProductDetail(productId);
    }
//     //todo 测试失败？？？ solution: 采用了“美的”为关键词 但他的status是2 无法查到
//    @RequestMapping(value = "/search")
//    @ResponseBody
//    public ServerResponse<PageInfo>  searchPortalProduct(@RequestParam(value = "keyword",required = false) String keyword,
//                                                         @RequestParam(value = "categoryId",required = false)Integer categoryId,
//                                                         @RequestParam(defaultValue = "1") int pageNum,
//                                                         @RequestParam(defaultValue = "10")int pageSize,
//                                                         @RequestParam(value = "orderBy",defaultValue = "") String orderBy) {
//        return iProductService.searchPortalProduct(keyword, categoryId, pageNum, pageSize, orderBy);
//    }
    @RequestMapping(value = "/{keyword}/{categoryId}/{pageNum}/{pageSize}/{orderBy}",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<PageInfo> searchPortalProduct(@PathVariable(value = "keyword")String keyword,
                                                @PathVariable(value = "categoryId")Integer categoryId,
                                                @PathVariable(value = "pageNum") Integer pageNum,
                                                @PathVariable(value = "pageSize") Integer pageSize,
                                                @PathVariable(value = "orderBy") String orderBy){
        if(pageNum == null){
            pageNum = 1;
        }
        if(pageSize == null){
            pageSize = 10;
        }
        if(StringUtils.isBlank(orderBy)){
            orderBy = "price_asc";
        }

        return iProductService.searchPortalProduct(keyword,categoryId,pageNum,pageSize,orderBy);
    }
}

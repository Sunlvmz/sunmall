package com.sun.sunmall.service;

import com.github.pagehelper.PageInfo;
import com.sun.sunmall.common.ServerResponse;
import com.sun.sunmall.pojo.Product;
import com.sun.sunmall.vo.ProductDetailVO;

/**
 * Created by sun on 2017/5/17.
 */
public interface IProductService {
    //后台服务
    ServerResponse saveOrUpdateProduct(Product product);
    ServerResponse setSaleStatus(Integer productId, Integer status);
    ServerResponse<ProductDetailVO> manageProductDetail(Integer productId);
    ServerResponse<PageInfo> getProductList(int pageNum, int pageSize);
    ServerResponse<PageInfo> searchProduct(String productName, Integer productId, int pageNum, int pageSize);

    //前台服务
    ServerResponse<ProductDetailVO> getPortalProductDetail(Integer productId);
    ServerResponse<PageInfo> searchPortalProduct(String keyword, Integer categoryId, int pageNum, int pageSize, String orderBy);
}

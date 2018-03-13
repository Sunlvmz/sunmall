package com.sun.sunmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.sun.sunmall.common.Const;
import com.sun.sunmall.common.ResponseCode;
import com.sun.sunmall.common.ServerResponse;
import com.sun.sunmall.dao.CategoryMapper;
import com.sun.sunmall.dao.ProductMapper;
import com.sun.sunmall.pojo.Category;
import com.sun.sunmall.pojo.Product;
import com.sun.sunmall.service.ICategoryService;
import com.sun.sunmall.service.IProductService;
import com.sun.sunmall.utils.DateTimeUtil;
import com.sun.sunmall.utils.PropertiesUtil;
import com.sun.sunmall.vo.ProductDetailVO;
import com.sun.sunmall.vo.ProductListVO;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.swing.*;
import java.util.List;

/**
 * Created by sun on 2017/5/17.
 */
@Service("iProductService")
public class ProductServiceImpl implements IProductService {
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ICategoryService iCategoryService;
    @Override
    public ServerResponse saveOrUpdateProduct(Product product) {//对后台保存更新一个逻辑；存在即更新，不存在则插入新增
        if(product!=null){
            if(StringUtils.isNotBlank((product.getSubImages()))){
                String[] subImageArray = product.getSubImages().split(",");//分割 .split(",") 与前端一致，以“，”分割 得到一个数组
                if(subImageArray.length>0){
                     product.setMainImage(subImageArray[0]);
                }
                if(product.getId()!=null){
                    int resultCount =productMapper.updateByPrimaryKey(product);
                    if(resultCount>0) return ServerResponse.createBySuccessMessage("更新产品成功");
                    else return ServerResponse.createByErrorMessage("更新产品失败");
                }else {
                    int resultCount =productMapper.insert(product);
                    if(resultCount>0) return ServerResponse.createBySuccessMessage("新增产品成功");
                    else return ServerResponse.createByErrorMessage("新增产品失败");
                }
            }
        }
        return ServerResponse.createByErrorMessage("未得到产品信息");
    }

    @Override
    public ServerResponse setSaleStatus(Integer productId, Integer status) {
        if (productId == 0 || status == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int resultCount = productMapper.updateByPrimaryKeySelective(product);
        if (resultCount > 0) {
            return ServerResponse.createBySuccessMessage("修改产品销售状态成功");
        }
        return ServerResponse.createByErrorMessage("修改产品状态失败");
    }

    @Override
    public ServerResponse<ProductDetailVO> manageProductDetail(Integer productId) {
        if (productId == 0) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return ServerResponse.createByErrorMessage("产品已下架或删除");
        }
        ProductDetailVO productDetailVO = assembleProductDetailVO(product);
        return ServerResponse.createBySuccess(productDetailVO);
    }

    @Override
    public ServerResponse<PageInfo> getProductList(int pageNum, int pageSize) {
        //startPage-start 记录开始
        PageHelper.startPage(pageNum, pageSize);
        //填充SQL查询逻辑
        List<Product> productList = productMapper.selectProductList();
        List<ProductListVO> productListVOlist = Lists.newArrayList();
        for (Product product : productList) {
            ProductListVO productListVO = assembleProductListVO(product);
            productListVOlist.add(productListVO);
        }
        //pageHelper收尾
        PageInfo pageResult = new PageInfo(productList);
        pageResult.setList(productListVOlist);
        return ServerResponse.createBySuccess(pageResult);
    }

    @Override
    public ServerResponse<PageInfo> searchProduct(String productName, Integer productId, int pageNum, int pageSize) {
        //startPage-start 记录开始
        PageHelper.startPage(pageNum, pageSize);
        //填充SQL查询逻辑
        if(StringUtils.isNotBlank(productName)){
            productName = new StringBuilder().append("%").append(productName).append("%").toString();//模糊化productName 便于模糊化查询。也可直接在SQL中模糊化查询
        }
        List<Product> productList = productMapper.selectByProductInfo(productName, productId);
        List<ProductListVO> productListVOlist = Lists.newArrayList();
        for (Product product : productList) {
            ProductListVO productListVO = assembleProductListVO(product);
            productListVOlist.add(productListVO);
        }
        //pageHelper收尾
        PageInfo pageResult = new PageInfo(productList);
        pageResult.setList(productListVOlist);
        return ServerResponse.createBySuccess(pageResult);
    }

    @Override
    public ServerResponse<ProductDetailVO> getPortalProductDetail(Integer productId) {
        if (productId == 0) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return ServerResponse.createByErrorMessage("产品不存在");
        }
        if(product.getStatus()!=Const.ProductStatus.ON_SALE.getCode()){
            return ServerResponse.createByErrorMessage("产品已下架或删除");
        }
        ProductDetailVO productDetailVO = assembleProductDetailVO(product);
        return ServerResponse.createBySuccess(productDetailVO);
    }

    @Override
    public ServerResponse<PageInfo> searchPortalProduct(String keyword, Integer categoryId,
                                                        int pageNum, int pageSize, String orderBy) {

        if(StringUtils.isBlank(keyword)&&categoryId==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        List<Integer> categoryIdList = Lists.newArrayList();

        if (categoryId != null) {
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            if (category == null&& StringUtils.isBlank(keyword)) {
                PageHelper.startPage(pageNum, pageSize);
                List<ProductListVO> productListVOList = Lists.newArrayList();
                PageInfo resultPage = new PageInfo(productListVOList);
                return ServerResponse.createBySuccess(resultPage);
            }
            categoryIdList = iCategoryService.getCategoryAndDeepChildrenCategory(category.getId()).getData();//todo 选择用category.getId() 而不是直接用categoryId
        }

        if (StringUtils.isNotBlank(keyword)) {
            keyword =new StringBuilder().append("%").append(keyword).append("%").toString();
        }

        PageHelper.startPage(pageNum, pageSize);

        if(StringUtils.isNotBlank(orderBy)){
            if(Const.ProductListOrderBy.ORDER_BY.contains(orderBy)){
                String[] orderByArray = orderBy.split("_");
                PageHelper.orderBy(orderByArray[0]+" " + orderByArray[1]);
            }
        }
        List<Product>  productList = productMapper.selectByKeywordAndCategoryIds(StringUtils.isBlank(keyword)?null:keyword,categoryIdList.size()==0?null:categoryIdList);

        List<ProductListVO> productListVOList =Lists.newArrayList();
        for (Product product : productList) {
            ProductListVO productListVO = assembleProductListVO(product);
            productListVOList.add(productListVO);
        }
        PageInfo pageInfo = new PageInfo(productListVOList);
        pageInfo.setList(productListVOList);
        return ServerResponse.createBySuccess(pageInfo);
    }

    private  ProductDetailVO assembleProductDetailVO(Product product){//类似格式化装配Bean
        ProductDetailVO productDetailVO = new ProductDetailVO();
        productDetailVO.setId(product.getId());
        productDetailVO.setName(product.getName());
        productDetailVO.setMainImage(product.getMainImage());
        productDetailVO.setSubtitle(product.getSubtitle());
        productDetailVO.setDetail(product.getDetail());
        productDetailVO.setCategoryId(product.getCategoryId());
        productDetailVO.setStatus(product.getStatus());
        productDetailVO.setPrice(product.getPrice());
        productDetailVO.setStock(product.getStock());
        productDetailVO.setSubImages(product.getSubImages());
        //imageHost 配置文件 隔离业务。方便部署更改
        productDetailVO.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.happymmall.com/"));
        // parentCategoryId
        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if (category == null) {
            productDetailVO.setParentCategoryId(0);
        }
        else productDetailVO.setParentCategoryId(category.getParentId());
        //createTime updateTime
        productDetailVO.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
        productDetailVO.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));
        return productDetailVO;
    }

    private  ProductListVO assembleProductListVO(Product product) {
        ProductListVO productlistVO = new ProductListVO();
        productlistVO.setId(product.getId());
        productlistVO.setName(product.getName());
        productlistVO.setMainImage(product.getMainImage());
        productlistVO.setSubtitle(product.getSubtitle());
        productlistVO.setCategoryId(product.getCategoryId());
        productlistVO.setStatus(product.getStatus());
        productlistVO.setPrice(product.getPrice());
        productlistVO.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.happymmall.com/"));
        return productlistVO;
    }

}

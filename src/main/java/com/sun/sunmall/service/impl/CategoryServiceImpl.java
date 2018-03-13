package com.sun.sunmall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.sun.sunmall.common.ServerResponse;
import com.sun.sunmall.dao.CategoryMapper;
import com.sun.sunmall.pojo.Category;
import com.sun.sunmall.service.ICategoryService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * Created by sun on 2017/5/15.
 */
@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService {
    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);
    @Autowired
    private CategoryMapper categoryMapper;
    @Override
    public ServerResponse addCategory(String categoryName, Integer parentId) {
        if(null == parentId|| StringUtils.isBlank(categoryName)){
            return ServerResponse.createByErrorMessage("添加分类参数错误");
        }
        Category newCategory = new Category();
        newCategory.setName(categoryName);
        newCategory.setParentId(parentId);
        newCategory.setStatus(true);

        int resultCount = categoryMapper.insertSelective(newCategory);
        if(resultCount>0){
            return ServerResponse.createBySuccess("添加分类成功");
        }
        return ServerResponse.createByErrorMessage("添加分类失败");

    }

    @Override
    public ServerResponse updateCategoryName(String categoryName, Integer categoryId) {
        if(null == categoryId|| StringUtils.isBlank(categoryName)){
            return ServerResponse.createByErrorMessage("添加分类参数错误");
        }
   int resultCount = categoryMapper.updateCategoryNameByPrimaryId(categoryName, categoryId);
        if(resultCount>0){
            return ServerResponse.createBySuccess("更新分类成功");
        }
        return ServerResponse.createByErrorMessage("更新分类失败");
    }

    @Override
    public ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId) {
    List<Category> categories = categoryMapper.selectCategoryChildrenById(categoryId);
    if(CollectionUtils.isEmpty(categories)){
            logger.info("未找到子分类");
    }
    return ServerResponse.createBySuccess(categories);
    }

    @Override
    public ServerResponse<List<Integer>> getCategoryAndDeepChildrenCategory(Integer categoryId) {
        Set<Category> categorySet = Sets.newHashSet();//guava提供的方法
        findChildCategory(categorySet,categoryId);
        List<Integer> categoryIdList = Lists.newArrayList();//guava提供
        if(categoryId!=null){
            for(Category c:categorySet){
                categoryIdList.add(c.getId());
            }
        }
        return ServerResponse.createBySuccess(categoryIdList);
    }

    //递归结构.  采用Category类 需要重写hashcode和equals方法，且判断因子一直
    private Set<Category> findChildCategory(Set<Category> categorySet,Integer id){
        Category category = categoryMapper.selectByPrimaryKey(id);
        if(category!= null){
            categorySet.add(category);
        }
        List<Category> categoryList = categoryMapper.selectCategoryChildrenById(id);//mybatis查询子节点，不会返回NULL，不必担心foreach里出现NUll异常
        for(Category c:categoryList){//递归退出条件：categoryList返回空，进不去循环，直接退出
            findChildCategory(categorySet,c.getId());
        }
        return categorySet;
    }
}

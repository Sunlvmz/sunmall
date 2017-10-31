package com.sun.sunmall.dao;

import com.sun.sunmall.pojo.Category;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CategoryMapper {
    int deleteByPrimaryKey(Integer id);
    int insert(Category record);
    int insertSelective(Category record);
    Category selectByPrimaryKey(Integer id);
    int updateByPrimaryKeySelective(Category record);
    int updateByPrimaryKey(Category record);
    int updateCategoryNameByPrimaryId(@Param("categoryName") String categoryName,@Param("categoryId") Integer categoryId);
    List<Category> selectCategoryChildrenById(Integer parentId);
}
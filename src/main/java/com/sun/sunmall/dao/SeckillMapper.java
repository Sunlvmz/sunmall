package com.sun.sunmall.dao;


import com.sun.sunmall.pojo.Product;
import com.sun.sunmall.pojo.Record;
import com.sun.sunmall.pojo.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeckillMapper {

    List<User> getAllUser();

    List<Product> getAllProduct();

    Product getProductById(Integer id);

    boolean updatePessLockInMySQL(Product product);

    boolean updatePosiLockInMySQL(Product product);

    boolean insertRecord(Record record);

    boolean updateByAsynPattern(Product product);
}

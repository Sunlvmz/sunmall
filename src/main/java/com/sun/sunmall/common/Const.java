package com.sun.sunmall.common;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * Created by sun on 2017/5/11.
 */
public class Const {
    public static final String CURRENT_USER = "curentUser";
    public static String USERNAME = "username";
    public static String EMAIL = "email";

   public  static int REDIS_SESSION_EXPIRETIME = 60*30;
    public static  final String TOKEN_PREFIX = "token_";

    public enum ProductStatus{
        ON_SALE(1,"在售");
        private final int code;
        private  final  String desc;
        ProductStatus(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }
        public int getCode() {
            return code;
        }
        public String getDesc() {
            return desc;
        }
    }


    public interface Role{   //枚举繁重，这里用内部接口分组
        int ROLE_CUSTOMER = 0;
        int ROLE_ADMIN =  1;
    }

    public interface Cart {
        int CHECKED = 1;
        int UNCHECKED = 0;
        String LIMIT_NUM_SUCCESS = "LIMIT_NUM_SUCCESS";
        String LIMIT_NUM_FAIL = "LIMIT_NUM_FAIL";

    }

    public interface ProductListOrderBy{
        Set<String>  ORDER_BY= Sets.newHashSet("PRICE_DSC", "PRICE_ASC");
    }

}

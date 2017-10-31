package com.sun.sunmall.utils;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 * Created by sun on 2017/5/12.
 */
public class MD5UtilTest {

    @Autowired
    private MD5Util md5Util;
    @Test
    public void MD5EncodeUtf8() throws Exception {
       String password= "admin";
        System.out.println(md5Util.MD5EncodeUtf8(password));

    }

}
package com.sun.sunmall.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.sunmall.pojo.User;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {
    //转成jsonarray
    public static String objectToJson(Object data) {
        String json = JSONArray.toJSONString(data);
        return json;
    }
    //string转java对象
    public static User jsonObjectToUser(String userString) {
        User user = JSONObject.parseObject(userString.toString(),User.class);
        return user;
    }
    public static List<User> objectToUser(JSONArray array){
        List<User> userList =new ArrayList<>();
        for (int i=0;i<array.size();i++) {
            System.out.print("array.get(i)    "+array.get(i).toString());
            //parseObject解析的是一个字符串，强转成为String是不行的，必须打印成String语句
            User user = JSONObject.parseObject(array.get(i).toString(),User.class);
            userList.add(user);
        }
        return userList;
    }
    /**
     * 序列化对象
     */
    public static String toJsonString(Object obj)
    {
        return JSON.toJSONString(obj);
    }

    /**
     * 序列化List数组
     */
    public static String toJsonArray(List<?> list){
        String jsonText = JSON.toJSONString(list, true);
        return jsonText;
    }

    /**
     * 反序列化为json对象
     */
    public static Object parseJsonObject(String text)
    {
        JSONObject json = JSON.parseObject(text);
        return json;
    }

    /**
     * 反序列化为javaBean对象
     */
    public static Object parseBeanObject(String text)
    {
        return JSON.parseObject(text);

    }

    /**
     * 将javaBean转化为json对象
     */
    public static Object bean2Json(Object obj)
    {
        JSONObject jsonObj = (JSONObject) JSON.toJSON(obj);
        return jsonObj;
    }
}

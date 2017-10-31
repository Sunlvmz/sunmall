package com.sun.sunmall.dao;

import com.sun.sunmall.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    List<User> listUser();

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int checkUsername(String username);
    int checkEmail(String email);
//java编程语言的一个问题，也就是java没有保存行参的记录，java在运行的时候会把List<Seckill> queryAll(int offset,int limit);中的参数变成这样:
// queryAll(int arg0,int arg1),
// 这样我们就没有办法去传递多个参数
    //加@Param 使我们的MyBatis识别多个参数，将Dao层方法中的多个参数与xml映射文件中sql语句的传入参数完成映射。
    User selectLogin(@Param("username") String username, @Param("password") String password);
    String selectQuestionByUsername(String username);
    int checkAnswer(@Param("username") String username, @Param("question") String password, @Param("answer") String answer);
    int updatePasswordByUsername(@Param("username") String username, @Param("passwordNew") String passwordNew);
    int checkPassword(@Param("password") String password, @Param("userId") int userId);
    int checkEmailByUserId(@Param("email") String email, @Param("userId") int userId);
}
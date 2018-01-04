package org.oj.data;

import org.apache.ibatis.annotations.Param;
import org.oj.model.javaBean.UserBean;

import java.util.List;

/**
 * Created by xanarry on 18-1-1.
 */
public interface User {
    //insert
    public int addNewUser(UserBean user);

    //delete
    public void deleteUserById(int userID);


    //update
    public void updateUser(UserBean user);

    //select
    public UserBean getUserByID(@Param("userID") int userID);


    public UserBean getUserByEmail(@Param("email") String email);


    public List<UserBean> getUserList(@Param("start") int start, @Param("count") int count);


    public boolean checkEmailExist(@Param("email") String email);

    public boolean checkUserNameExist(@Param("userName") String userName);

    public boolean checkPasswordByUserName(@Param("userName") String userName, @Param("password") String password);

    public boolean checkPasswordByEmail(@Param("email") String email, @Param("password") String password);

    //public List<UserBean>
}

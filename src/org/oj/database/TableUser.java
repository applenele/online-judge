package org.oj.database;

import org.apache.ibatis.annotations.Param;
import org.oj.model.javaBean.UserBean;

import java.util.List;

/**
 * Created by xanarry on 18-1-1.
 */
public interface TableUser extends BaseFunction{
    //insert
    int addNewUser(@Param("user") UserBean user);



    //delete
    void deleteUserById(@Param("userID") Integer userID);



    //update
    void updateUser(@Param("user") UserBean user);
    void updateSubmittedTimes(@Param("userID") int userID);
    void updateAcceptedTimes(@Param("userID") int userID);



    //select one
    UserBean getUserByID(@Param("userID") int userID);
    UserBean getUserByEmail(@Param("email") String email);


    //select list
    List<UserBean> getUserList(@Param("start") int start, @Param("count") int count);
    List<UserBean> getChart(@Param("start") int start, @Param("count") int count);

    //check
    boolean checkEmailExist(@Param("email") String email);
    boolean checkUserNameExist(@Param("userName") String userName);

}

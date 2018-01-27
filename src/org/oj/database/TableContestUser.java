package org.oj.database;

import org.apache.ibatis.annotations.Param;
import org.oj.model.javaBean.ContestUserBean;

import java.util.List;

/**
 * Created by xanarry on 18-1-1.
 */
public interface TableContestUser extends BaseFunction {
    int addUser(@Param("contestUser") ContestUserBean contestUser);

    void deleteUser(@Param("contestID") Integer contestID, @Param("userID") Integer userID);

    boolean checkUserRegistered(@Param("contestID") int contestID, @Param("userID") Integer userID);

    List<ContestUserBean> getContestUserList(@Param("contestID") Integer contestID);

    Integer getContestUserCount(@Param("contestID") Integer contestID);
}

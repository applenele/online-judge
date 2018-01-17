package org.oj.database;

import org.apache.ibatis.annotations.Param;
import org.oj.model.javaBean.ContestUserBean;

import java.util.List;

/**
 * Created by xanarry on 18-1-1.
 */
public interface TableContestUser {
    public int addUser(@Param("contestUser") ContestUserBean contestUser);

    public void deleteUser(@Param("contestID") Integer contestID, @Param("userID") Integer userID);

    public List<ContestUserBean> getContestUserList(@Param("contestID") Integer contestID);
}

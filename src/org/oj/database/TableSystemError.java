package org.oj.database;

import org.apache.ibatis.annotations.Param;
import org.oj.model.javaBean.SystemErrorBean;

public interface TableSystemError {
    void addErrorMessage(@Param("systemError") SystemErrorBean systemError);
    void deleteErrorMessage(@Param("submitID") Integer submitID);
    SystemErrorBean getSystemErrorMessage(@Param("submitID") Integer submitID);
}

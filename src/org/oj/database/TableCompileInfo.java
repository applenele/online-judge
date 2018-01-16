package org.oj.database;

import org.apache.ibatis.annotations.Param;
import org.oj.model.javaBean.CompileInfoBean;

/**
 * Created by xanarry on 18-1-1.
 */
public interface TableCompileInfo {
    //insert
    public int insertCompileResult(@Param("compileResult") CompileInfoBean compileResult);

    //delete
    public void deleteCompileResult(@Param("submitID") int submitID);

    //update  needn't update function

    //select
    public CompileInfoBean getCompileResult(@Param("submitID") int submitID);

}

package org.oj.database;

import org.apache.ibatis.annotations.Param;
import org.oj.model.javaBean.SourceCodeBean;

/**
 * Created by xanarry on 18-1-1.
 */
public interface SourceCode {
    public int insert(@Param("sourceCode") SourceCodeBean sourceCode);

    public void deleteSourceCode(@Param("submitID") int submitID);

    public SourceCodeBean getSourceCode(@Param("submitID") int submitID);
}

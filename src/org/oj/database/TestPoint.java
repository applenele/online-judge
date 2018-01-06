package org.oj.database;

import org.apache.ibatis.annotations.Param;
import org.oj.model.javaBean.TestPointBean;

import java.util.List;

/**
 * Created by xanarry on 18-1-5.
 */
public interface TestPoint {
    public int addTestPoint(@Param("testPoint") TestPointBean testPoint);

    public void deleteTestPoint(@Param("problemID") int problemID, @Param("testPointID") int testPointID);

    public void updateTestPoint(@Param("testPoint") TestPoint testPoint);

    public List<TestPointBean> getTestPointList(@Param("problemID") int problemID);

    public TestPointBean getTestPoint(@Param("problemID") int problemID, @Param("testPointID") int testPointID);
}

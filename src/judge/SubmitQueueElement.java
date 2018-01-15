package judge;

import judge.beans.SubmitBean;
import org.oj.model.javaBean.SubmitRecordBean;

public class SubmitQueueElement {
    private SubmitRecordBean submitRecordBean;
    private SubmitBean submitBean;

    public SubmitQueueElement() { }

    public SubmitQueueElement(SubmitRecordBean submitRecordBean, SubmitBean submitBean) {
        this.submitRecordBean = submitRecordBean;
        this.submitBean = submitBean;
    }

    public SubmitRecordBean getSubmitRecordBean() {
        return submitRecordBean;
    }

    public void setSubmitRecordBean(SubmitRecordBean submitRecordBean) {
        this.submitRecordBean = submitRecordBean;
    }

    public SubmitBean getSubmitBean() {
        return submitBean;
    }

    public void setSubmitBean(SubmitBean submitBean) {
        this.submitBean = submitBean;
    }
}

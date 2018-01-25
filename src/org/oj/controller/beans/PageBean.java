package org.oj.controller.beans;

public class PageBean {
    private int currentPageVal;//当前页面
    private int countPerPage;//每页记录数
    private int maxPageVal;//最大页码
    private int recordCount;//记录总数
    private String url;

    public PageBean() {}

    public PageBean(int currentPageVal, int countPerPage, int maxPageVal, int recordCount, String url) {
        this.currentPageVal = currentPageVal;
        this.countPerPage = countPerPage;
        this.maxPageVal = maxPageVal;
        this.recordCount = recordCount;
        this.url = url;
    }


    public int getCurrentPageVal() {
        return currentPageVal;
    }

    public void setCurrentPageVal(int currentPageVal) {
        this.currentPageVal = currentPageVal;
    }

    public int getCountPerPage() {
        return countPerPage;
    }

    public void setCountPerPage(int countPerPage) {
        this.countPerPage = countPerPage;
    }

    public int getMaxPageVal() {
        return maxPageVal;
    }

    public void setMaxPageVal(int maxPageVal) {
        this.maxPageVal = maxPageVal;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "PageBean{" +
                "currentPageVal=" + currentPageVal +
                ", countPerPage=" + countPerPage +
                ", maxPageVal=" + maxPageVal +
                ", recordCount=" + recordCount +
                ", url='" + url + '\'' +
                '}';
    }
}

package org.oj.database;

import org.oj.model.javaBean.LanguageBean;

import java.util.List;

/**
 * Created by xanarry on 18-1-1.
 */
public interface Language {
    public void insertLanguage(LanguageBean languageBean);
    public LanguageBean getLanguageByID(int languageID);
    public short getLanguageID(String language);
    public List<LanguageBean> getLanguageList();
}

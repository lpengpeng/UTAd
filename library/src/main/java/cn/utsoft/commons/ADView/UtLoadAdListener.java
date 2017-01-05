package cn.utsoft.commons.ADView;

/**
 * Create by 李俊鹏 on 2016/12/26 11:07
 * Function：
 * Desc：
 */

public interface UtLoadAdListener {
    void loadSuccess(String s);

    void loadFailure(Exception e);
}

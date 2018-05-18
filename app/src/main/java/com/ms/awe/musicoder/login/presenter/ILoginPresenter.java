package com.ms.awe.musicoder.login.presenter;

/**
 * Created by awe on 2018/5/17.
 *
 */

public interface ILoginPresenter {

    //清空内容
    void clear();
    //登录
    void doLogin(String name ,String passwd);
    //显示进度条
    void setProgressBarVisibility(int visibility);
}

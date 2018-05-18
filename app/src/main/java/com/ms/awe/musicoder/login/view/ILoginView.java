package com.ms.awe.musicoder.login.view;

/**
 * Created by awe on 2018/5/17.
 */

public interface ILoginView {

    public void onClearText();

    public void onLoginResult(Boolean result , int code);

    public void onSetProgressBarVisibility(int visibility);
}

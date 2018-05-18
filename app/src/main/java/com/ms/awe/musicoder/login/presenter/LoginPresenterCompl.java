package com.ms.awe.musicoder.login.presenter;

import android.os.Handler;
import android.os.Looper;

import com.ms.awe.musicoder.login.model.IUser;
import com.ms.awe.musicoder.login.model.UserModel;
import com.ms.awe.musicoder.login.view.ILoginView;


/**
 * Created by awe on 2018/5/17.
 * LoginPresenter的实现类
 */

public class LoginPresenterCompl implements ILoginPresenter {

    ILoginView iLoginView;
    IUser user;
    Handler handler;    //Handler导入os包！！！

    public LoginPresenterCompl(ILoginView iLoginView) {
        this.iLoginView = iLoginView;
        initUser();
        handler = new Handler(Looper.getMainLooper());
    }

    /**
     * 初始化用户信息
     */
    private void initUser() {
        user = new UserModel("msxg123", "dby123456");
    }

    /**
     * 清空EditText
     */
    @Override
    public void clear() {
        iLoginView.onClearText();
    }

    /**
     *  login功能
     */
    @Override
    public void doLogin(String name, String passwd) {
        Boolean isLoginSuccess = true;
        final int code = user.checkUserValidity(name, passwd);
        if (code != 0) {
            isLoginSuccess = false;
        }
        final Boolean result = isLoginSuccess;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                iLoginView.onLoginResult(result, code);
            }
        }, 1000);
    }

    /**
     *  设置ProgressBar的可见属性
     */
    @Override
    public void setProgressBarVisibility(int visibility) {
        iLoginView.onSetProgressBarVisibility(visibility);
    }
}

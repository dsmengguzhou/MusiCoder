package com.ms.awe.musicoder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ms.awe.musicoder.R;
import com.ms.awe.musicoder.login.presenter.ILoginPresenter;
import com.ms.awe.musicoder.login.presenter.LoginPresenterCompl;
import com.ms.awe.musicoder.login.view.ILoginView;

/**
 * Created by awe on 2018/5/17.
 */

public class LoginActivity extends ActionBarActivity implements ILoginView,View.OnClickListener{

    private EditText editUser;
    private EditText editPass;
    private Button btnLogin;
    private Button btnClear;
    ILoginPresenter loginPresenter;
    private ProgressBar progressBar;
    private ImageView imgFrog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
    }

    private void initViews() {
        //find view
        editUser = (EditText) findViewById(R.id.et_username);
        editPass = (EditText) findViewById(R.id.et_password);
        btnLogin = (Button) findViewById(R.id.btn_do_login);
        btnClear = (Button) findViewById(R.id.btn_do_clear);
        progressBar = (ProgressBar) findViewById(R.id.progress_login);
        imgFrog = (ImageView) findViewById(R.id.img_frog);

        //set listener
        btnLogin.setOnClickListener(this);
        btnClear.setOnClickListener(this);
        imgFrog.setOnClickListener(this);

        //init
        loginPresenter = new LoginPresenterCompl(this);
        loginPresenter.setProgressBarVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_do_login:
                loginPresenter.setProgressBarVisibility(View.VISIBLE);
                btnLogin.setEnabled(false);
                btnClear.setEnabled(false);
                loginPresenter.doLogin(editUser.getText().toString(),
                        editPass.getText().toString());
                break;
            case R.id.btn_do_clear:
                loginPresenter.clear();
                break;
            case R.id.img_frog:
                finish();
                break;
        }
    }

    @Override
    public void onClearText() {
        editUser.setText("");
        editPass.setText("");
    }

    @Override
    public void onLoginResult(Boolean result, int code) {
        loginPresenter.setProgressBarVisibility(View.INVISIBLE);
        btnLogin.setEnabled(true);
        btnClear.setEnabled(true);
        if (result){
            Toast.makeText(this,"Login Success",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this,LoginSuccessActivity.class);
            startActivity(intent);
            finish();
        }else {
            Toast.makeText(this,"账号密码输入错误",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSetProgressBarVisibility(int visibility) {
        progressBar.setVisibility(visibility);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

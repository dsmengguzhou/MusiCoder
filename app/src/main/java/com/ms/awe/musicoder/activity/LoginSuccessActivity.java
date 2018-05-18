package com.ms.awe.musicoder.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.ms.awe.musicoder.R;

/**
 * Created by awe on 2018/5/17.
 */

public class LoginSuccessActivity extends AppCompatActivity{

    private ImageView imageView;
    private ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_success);

        imageView = (ImageView) findViewById(R.id.iv_detail);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}

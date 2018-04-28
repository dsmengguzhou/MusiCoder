package com.ms.awe.musicoder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.ms.awe.musicoder.R;
import com.squareup.picasso.Picasso;

/**
 * Created by awe on 2018/4/28.
 */

public class RecyclerDetailActivity extends AppCompatActivity{

    private ImageView imageView;
    private ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_detail);

        Intent intent = getIntent();                    //获取传来的intent对象
        String mUrl = intent.getStringExtra("url");     //获取键值对的键名

        imageView = (ImageView) findViewById(R.id.iv_detail);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (!TextUtils.isEmpty(mUrl)){
            Picasso.with(RecyclerDetailActivity.this)
                    .load(mUrl)
                    .into(imageView);
        }
    }
}

package com.ms.awe.musicoder.activity;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.ms.awe.musicoder.R;

/**
 * Created by awe on 2018/5/18.
 */

public class AnswerQuestionActivity extends AppCompatActivity implements View.OnClickListener{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_question);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_back://点击头部返回

                break;

            case R.id.tv_share://点击头部分享

                break;
            default:
                break;
        }
    }




}

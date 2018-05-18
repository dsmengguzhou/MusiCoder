package com.ms.awe.musicoder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.ms.awe.musicoder.R;

/**
 * Created by awe on 2018/5/18.
 */

public class AnswerActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView imgTiger;
    private Button btnAnswerQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        initViews();
    }

    private void initViews() {
        imgTiger = (ImageView) findViewById(R.id.img_tiger);
        btnAnswerQuestion = (Button) findViewById(R.id.btn_answer1);

        imgTiger.setOnClickListener(this);
        btnAnswerQuestion.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.img_tiger:
                finish();
                break;
            case R.id.btn_answer1:
                intent = new Intent(AnswerActivity.this,AnswerQuestionActivity.class);
                startActivity(intent);
                break;
            default:

                break;
        }
    }
}

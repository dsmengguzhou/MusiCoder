package com.ms.awe.musicoder.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ms.awe.musicoder.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnMD;       //Material Design模块内容
    private Button btnRecycler; //RecyclerView模块内容
    private Button btnLogin;    //Mvp案例Login页面
    private Button btnAnswer;   //答题部分

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_main);

        initViews();


    }

    private void initViews() {
        btnMD = (Button) findViewById(R.id.btn_md);
        btnRecycler = (Button) findViewById(R.id.btn_recycler);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnAnswer = (Button) findViewById(R.id.btn_answer_question);

        btnMD.setOnClickListener(this);
        btnRecycler.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnAnswer.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.btn_md:
                intent = new Intent(MainActivity.this, MaterialActivity.class);
                startActivity(intent);
                Toast.makeText(MainActivity.this,"Material Design",Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_recycler:
                intent = new Intent(MainActivity.this, RecyclerActivity.class);
                startActivity(intent);
                Toast.makeText(MainActivity.this,"RecyclerView",Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_login:
                intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                Toast.makeText(MainActivity.this,"Mvp之Login页面",Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_answer_question:
                intent = new Intent(MainActivity.this, AnswerActivity.class);
                startActivity(intent);
                Toast.makeText(MainActivity.this,"答题部分",Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(MainActivity.this,"你点类啥？？？",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}

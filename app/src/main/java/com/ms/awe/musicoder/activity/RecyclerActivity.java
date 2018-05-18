package com.ms.awe.musicoder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.ms.awe.musicoder.R;

/**
 * Created by awe on 2018/4/24.
 */

public class RecyclerActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnLine;
    private Button btnSmoothLine;
    private Button btnGrid;
    private Button btnStragged;
    private ImageView imgElephant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_recycler);
        initViews();


    }

    private void initViews() {
        btnLine = (Button) findViewById(R.id.btn_line);
        btnSmoothLine = (Button) findViewById(R.id.btn_smoothline);
        btnGrid = (Button) findViewById(R.id.btn_grid);
        btnStragged = (Button) findViewById(R.id.btn_stragged);
        imgElephant = (ImageView) findViewById(R.id.img_elephant);

        btnLine.setOnClickListener(this);
        btnSmoothLine.setOnClickListener(this);
        btnGrid.setOnClickListener(this);
        btnStragged.setOnClickListener(this);
        imgElephant.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btn_line:
                intent = new Intent(RecyclerActivity.this, LineActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_smoothline:
                intent = new Intent(RecyclerActivity.this, SmoothLineActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_grid:
                intent = new Intent(RecyclerActivity.this,GridActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_stragged:
                intent = new Intent(RecyclerActivity.this,StaggeredActivity.class);
                startActivity(intent);
                break;
            case R.id.img_elephant:
                finish();
                break;
            default:

                break;
        }
    }
}

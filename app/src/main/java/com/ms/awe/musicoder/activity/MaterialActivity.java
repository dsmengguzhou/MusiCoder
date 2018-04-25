package com.ms.awe.musicoder.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ms.awe.musicoder.R;

/**
 * Created by awe on 2018/4/24.
 */

public class MaterialActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnMaterial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_material);
        initViews();
    }

    private void initViews() {
        btnMaterial = (Button) findViewById(R.id.btn_material);
        btnMaterial.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_material:
                Toast.makeText(MaterialActivity.this,"迟点总结这部分",Toast.LENGTH_SHORT).show();
                break;
            default:

                break;
        }
    }
}

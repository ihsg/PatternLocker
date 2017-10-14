package com.github.ihsg.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PatternSettingActivity.startAction(MainActivity.this);
            }
        });

        findViewById(R.id.btn_checking).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PatternCheckingActivity.startAction(MainActivity.this);
            }
        });
    }
}
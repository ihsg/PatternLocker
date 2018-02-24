package com.github.ihsg.demo.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.ihsg.demo.R;
import com.github.ihsg.demo.ui.def.DefaultStyleActivity;
import com.github.ihsg.demo.ui.simple.SimpleStyleActivity;
import com.github.ihsg.demo.ui.whole.WholeStyleActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_default).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DefaultStyleActivity.startAction(MainActivity.this);
            }
        });

        findViewById(R.id.btn_simple).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleStyleActivity.startAction(MainActivity.this);
            }
        });

        findViewById(R.id.btn_whole).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WholeStyleActivity.startAction(MainActivity.this);
            }
        });
    }
}
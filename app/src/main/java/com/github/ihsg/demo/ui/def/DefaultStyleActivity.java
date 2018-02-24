package com.github.ihsg.demo.ui.def;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.ihsg.demo.R;


public class DefaultStyleActivity extends AppCompatActivity {

    public static void startAction(Context context) {
        Intent intent = new Intent(context, DefaultStyleActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_style);

        findViewById(R.id.btn_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DefaultPatternSettingActivity.startAction(DefaultStyleActivity.this);
            }
        });

        findViewById(R.id.btn_checking).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DefaultPatternCheckingActivity.startAction(DefaultStyleActivity.this);
            }
        });
    }
}

package com.github.ihsg.demo.ui.whole;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.ihsg.demo.R;

public class WholeStyleActivity extends AppCompatActivity {

    public static void startAction(Context context) {
        Intent intent = new Intent(context, WholeStyleActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whole_style);

        findViewById(R.id.btn_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WholePatternSettingActivity.startAction(WholeStyleActivity.this);
            }
        });

        findViewById(R.id.btn_checking).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WholePatternCheckingActivity.startAction(WholeStyleActivity.this);
            }
        });
    }
}

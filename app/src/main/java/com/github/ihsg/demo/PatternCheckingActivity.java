package com.github.ihsg.demo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public class PatternCheckingActivity extends AppCompatActivity {
    public static void startAction(Context context) {
        Intent intent = new Intent(context, PatternCheckingActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pattern_checking);
    }
}

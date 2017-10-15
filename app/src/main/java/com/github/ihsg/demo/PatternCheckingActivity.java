package com.github.ihsg.demo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.github.ihsg.patternlocker.OnPatternChangeListener;
import com.github.ihsg.patternlocker.PatternIndicatorView;
import com.github.ihsg.patternlocker.PatternLockerView;
import com.github.ihsg.patternlocker.ResultState;

import java.util.List;


public class PatternCheckingActivity extends AppCompatActivity {
    private PatternLockerView patternLockerView;
    private PatternIndicatorView patternIndicatorView;
    private TextView textMsg;
    private PatternHelper patternHelper;

    public static void startAction(Context context) {
        Intent intent = new Intent(context, PatternCheckingActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pattern_checking);

        this.patternIndicatorView = (PatternIndicatorView) findViewById(R.id.pattern_indicator_view);
        this.patternLockerView = (PatternLockerView) findViewById(R.id.pattern_lock_view);
        this.textMsg = (TextView) findViewById(R.id.text_msg);

        this.patternLockerView.setOnPatternChangedListener(new OnPatternChangeListener() {
            @Override
            public void onStart(PatternLockerView view) {
                patternIndicatorView.updateState(null, ResultState.OK);
            }

            @Override
            public void onChange(PatternLockerView view, List<Integer> hitList) {
                patternIndicatorView.updateState(hitList, ResultState.OK);
            }

            @Override
            public void onComplete(PatternLockerView view, List<Integer> hitList) {
                ResultState resultState = isPatternOk(hitList) ? ResultState.OK : ResultState.ERROR;
                view.setResultState(resultState);
                patternIndicatorView.updateState(hitList, resultState);
                updateMsg();
            }

            @Override
            public void onClear(PatternLockerView view) {
                finishIfNeeded();
            }
        });

        this.textMsg.setText("绘制解锁图案");
        this.patternHelper = new PatternHelper();
    }

    private boolean isPatternOk(List<Integer> hitList) {
        this.patternHelper.validateForChecking(hitList);
        return this.patternHelper.isOk();
    }

    private void updateMsg() {
        this.textMsg.setText(this.patternHelper.getMessage());
        this.textMsg.setTextColor(this.patternHelper.isOk() ?
                getResources().getColor(R.color.colorPrimary) :
                getResources().getColor(R.color.colorAccent));
    }

    private void finishIfNeeded() {
        if (this.patternHelper.isFinish()) {
            finish();
        }
    }
}

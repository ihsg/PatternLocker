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

public class PatternSettingActivity extends AppCompatActivity {

    private PatternLockerView patternLockerView;
    private PatternIndicatorView patternIndicatorView;
    private TextView textMsg;

    public static void startAction(Context context) {
        Intent intent = new Intent(context, PatternSettingActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pattern_setting);

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
                startTimer();
            }

            @Override
            public void onClear(PatternLockerView view) {
//                patternIndicatorView.updateState(null, ResultState.OK);
            }
        });

        this.textMsg.setText("设置解锁图案");
    }

    private boolean isPatternOk(List<Integer> hitList) {
        PatternUtil.getInstance().validateForSetting(hitList);
        return PatternUtil.getInstance().isOk();
    }

    private void updateMsg() {
        this.textMsg.setText(PatternUtil.getInstance().getMessage());
        this.textMsg.setTextColor(PatternUtil.getInstance().isOk() ? getResources().getColor(R.color.colorPrimary) : getResources().getColor(R.color.colorAccent));
    }

    private void startTimer() {
        this.patternLockerView.setEnabled(false);
        this.patternLockerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                patternLockerView.clearHitState();
                patternLockerView.setEnabled(true);
                finishIfNeeded();
            }
        }, 1000);
    }

    private void finishIfNeeded() {
        if (PatternUtil.getInstance().isFinish()) {
            finish();
        }
    }
}
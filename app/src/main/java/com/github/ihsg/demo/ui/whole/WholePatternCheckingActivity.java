package com.github.ihsg.demo.ui.whole;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.github.ihsg.demo.R;
import com.github.ihsg.demo.util.PatternHelper;
import com.github.ihsg.patternlocker.IHitCellView;
import com.github.ihsg.patternlocker.OnPatternChangeListener;
import com.github.ihsg.patternlocker.PatternIndicatorView;
import com.github.ihsg.patternlocker.PatternLockerView;

import java.util.List;

public class WholePatternCheckingActivity extends AppCompatActivity {

    private PatternLockerView patternLockerView;
    private PatternIndicatorView patternIndicatorView;
    private TextView textMsg;
    private PatternHelper patternHelper;

    public static void startAction(Context context) {
        Intent intent = new Intent(context, WholePatternCheckingActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whole_pattern_checking);

        this.patternIndicatorView = findViewById(R.id.pattern_indicator_view);
        this.patternLockerView = findViewById(R.id.pattern_lock_view);
        this.textMsg = findViewById(R.id.text_msg);

        final IHitCellView hitCellView = new RippleLockerHitCellView()
                .setHitColor(this.patternLockerView.getHitColor())
                .setErrorColor(this.patternLockerView.getErrorColor());

        this.patternLockerView.setHitCellView(hitCellView)
                .setLinkedLineView(null)
                .build();

        this.patternLockerView.setOnPatternChangedListener(new OnPatternChangeListener() {
            @Override
            public void onStart(PatternLockerView view) {
            }

            @Override
            public void onChange(PatternLockerView view, List<Integer> hitList) {
            }

            @Override
            public void onComplete(PatternLockerView view, List<Integer> hitList) {
                boolean isError = !isPatternOk(hitList);
                view.updateStatus(isError);
                patternIndicatorView.updateState(hitList, isError);
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
                getResources().getColor(R.color.colorPrimaryDark) :
                getResources().getColor(R.color.color_red));
    }

    private void finishIfNeeded() {
        if (this.patternHelper.isFinish()) {
            finish();
        }
    }
}

package com.github.ihsg.patternlocker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hsg on 20/09/2017.
 */

public class PatternIndicatorView extends View {
    private Paint paint;
    private ResultState resultState;
    private List<Integer> hitList;
    private List<CellBean> cellBeanList;

    public PatternIndicatorView(Context context) {
        this(context, null);
    }

    public PatternIndicatorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PatternIndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    public void updateState(List<Integer> hitList, ResultState resultState) {
        //1. reset to default state
        if (!this.hitList.isEmpty()) {
            for (int i : this.hitList) {
                this.cellBeanList.get(i).isHit = false;
            }
            this.hitList.clear();
        }

        //2. update hit state
        if (hitList != null) {
            this.hitList.addAll(hitList);
        }
        if (!this.hitList.isEmpty()) {
            for (int i : this.hitList) {
                this.cellBeanList.get(i).isHit = true;
            }
        }

        //3. update result
        this.resultState = resultState;

        //4. update view
        postInvalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int a = Math.min(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(a, a);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.cellBeanList == null) {
            this.cellBeanList = new CellFactory(getWidth(), getHeight()).getCellBeanList();
        }
        drawLine(canvas);
        drawCircles(canvas);
    }

    private void init(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this.paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.paint.setColor(Config.getDefaultColor());
        this.paint.setStrokeWidth(Config.getLineWidth(getResources()) / 2f);

        this.hitList = new ArrayList<>();
    }

    private void drawLine(Canvas canvas) {
        if (!this.hitList.isEmpty()) {
            Path path = new Path();
            CellBean first = this.cellBeanList.get(hitList.get(0));
            path.moveTo(first.x, first.y);
            for (int i = 1; i < hitList.size(); i++) {
                CellBean c = this.cellBeanList.get(hitList.get(i));
                path.lineTo(c.x, c.y);
            }

            this.paint.setColor(Config.getColorByState(this.resultState));
            this.paint.setStyle(Paint.Style.STROKE);
            canvas.drawPath(path, this.paint);
        }
    }

    private void drawCircles(Canvas canvas) {
        for (int i = 0; i < this.cellBeanList.size(); i++) {
            CellBean item = this.cellBeanList.get(i);
            if (item.isHit) {
                this.paint.setColor(Config.getColorByState(this.resultState));
                this.paint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(item.x, item.y, item.radius - this.paint.getStrokeWidth() / 2f, paint);
            } else {
                this.paint.setColor(Config.getDefaultColor());
                this.paint.setStyle(Paint.Style.STROKE);
                canvas.drawCircle(item.x, item.y, item.radius - this.paint.getStrokeWidth() / 2f, paint);
            }
        }
    }
}
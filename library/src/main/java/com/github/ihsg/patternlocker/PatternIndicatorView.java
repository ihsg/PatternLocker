package com.github.ihsg.patternlocker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

/**
 * Created by hsg on 20/09/2017.
 */

public class PatternIndicatorView extends View implements ManagerCenter.ChangeListener {

    private Paint paint;
    private List<CellBean> cellBeanList;
    private ResultState resultState;

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
        this.resultState = ManagerCenter.getInstance().getResultState();
        drawLine(canvas);
        drawCircles(canvas);
    }

    private void init(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this.paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.paint.setColor(Config.getDefaultColor());
        this.paint.setStrokeWidth(Config.getLineWidth(getResources()) / 2f);
        ManagerCenter.getInstance().setChangeListener(this);
    }

    private void drawLine(Canvas canvas) {
        List<Integer> hitList = ManagerCenter.getInstance().getHitList();
        if (!hitList.isEmpty()) {
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

    private void updateHitState() {
        for (CellBean c : this.cellBeanList) {
            c.isHit = false;
        }
        List<Integer> hitList = ManagerCenter.getInstance().getHitList();
        if (!hitList.isEmpty()) {
            for (int i : hitList) {
                this.cellBeanList.get(i).isHit = true;
            }
        }
    }

    @Override
    public void onChanged() {
        updateHitState();
        postInvalidate();
    }
}

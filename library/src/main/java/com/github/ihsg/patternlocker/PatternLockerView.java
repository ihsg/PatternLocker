package com.github.ihsg.patternlocker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;

/**
 * Created by hsg on 20/09/2017.
 */

public class PatternLockerView extends View {
    private List<CellBean> cellBeanList;
    private Paint paint;
    private ResultState resultState;
    private float endX;
    private float endY;

    public PatternLockerView(Context context) {
        this(context, null);
    }

    public PatternLockerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PatternLockerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(context, attrs, defStyleAttr);
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean isHandle = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                handleActionDown(event);
                isHandle = true;
                break;
            case MotionEvent.ACTION_MOVE:
                handleActionMove(event);
                isHandle = true;
                break;
            case MotionEvent.ACTION_UP:
                handleActionUp(event);
                isHandle = true;
                break;
            default:
                break;
        }
        postInvalidate();
        return isHandle ? true : super.onTouchEvent(event);
    }

    private void init(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this.paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.paint.setDither(true);
        this.paint.setAntiAlias(true);
        this.paint.setStrokeJoin(Paint.Join.ROUND);
        this.paint.setStrokeCap(Paint.Cap.ROUND);
        this.paint.setColor(Config.getDefaultColor());
        this.paint.setStrokeWidth(Config.getLineWidth(getResources()));
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

            if (((this.endX != 0) || (this.endY != 0)) && (hitList.size() < 9)) {
                path.lineTo(this.endX, this.endY);
            }

            this.paint.setColor(Config.getColorByState(this.resultState));
            this.paint.setStyle(Paint.Style.STROKE);
            canvas.drawPath(path, this.paint);
        }
    }

    private void drawCircles(Canvas canvas) {
        this.paint.setStyle(Paint.Style.FILL);
        for (int i = 0; i < this.cellBeanList.size(); i++) {
            CellBean item = this.cellBeanList.get(i);
            if (item.isHit) {
                this.paint.setColor(Config.getColorByState(this.resultState));
                canvas.drawCircle(item.x, item.y, item.radius, paint);

                this.paint.setColor(Config.getFillColor());
                canvas.drawCircle(item.x, item.y, item.radius - this.paint.getStrokeWidth(), this.paint);

                this.paint.setColor(Config.getColorByState(this.resultState));
                canvas.drawCircle(item.x, item.y, item.radius / 5f, paint);
            } else {
                this.paint.setColor(Config.getDefaultColor());
                canvas.drawCircle(item.x, item.y, item.radius, paint);
                this.paint.setColor(Config.getFillColor());
                canvas.drawCircle(item.x, item.y, item.radius - this.paint.getStrokeWidth(), this.paint);
            }
        }
    }

    private void handleActionDown(MotionEvent event) {
        //1. clear pre state
        List<Integer> hitList = ManagerCenter.getInstance().getHitList();
        for (int i = 0; i < hitList.size(); i++) {
            this.cellBeanList.get(hitList.get(i)).isHit = false;
        }
        ManagerCenter.getInstance().clearHit();
        ManagerCenter.getInstance().setResultState(ResultState.OK);

        //2. update hit state
        updateHitState(event);
    }

    private void handleActionMove(MotionEvent event) {
        //1. update hit resultState
        updateHitState(event);

        //2. update end point
        this.endX = event.getX();
        this.endY = event.getY();
    }

    private void handleActionUp(MotionEvent event) {
        //1. update hit resultState
        updateHitState(event);
        this.endX = 0;
        this.endY = 0;
        //2. check result
        ManagerCenter.getInstance().checkResult();
    }

    private void updateHitState(MotionEvent event) {
        final float x = event.getX();
        final float y = event.getY();
        for (CellBean c : this.cellBeanList) {
            if (!c.isHit && c.of(x, y)) {
                c.isHit = true;
                ManagerCenter.getInstance().addHit(c.id);
            }
        }
    }
}
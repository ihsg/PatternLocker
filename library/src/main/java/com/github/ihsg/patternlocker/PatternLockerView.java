package com.github.ihsg.patternlocker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hsg on 20/09/2017.
 */

public class PatternLockerView extends View {
    private float endX;
    private float endY;
    private int hitSize;
    private Paint paint;
    private ResultState resultState;
    private List<CellBean> cellBeanList;
    private List<Integer> hitList;
    private OnPatternChangeListener listener;

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

    public void setResultState(ResultState resultState) {
        this.resultState = resultState;
    }

    public void setOnPatternChangedListener(OnPatternChangeListener listener) {
        this.listener = listener;
    }

    public void clearHitState() {
        clearHitData();
        if (this.listener != null) {
            this.listener.onClear(this);
        }
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return super.onTouchEvent(event);
        }

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
        this.paint.setStrokeWidth(Config.getLineWidth(getResources()));

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
        //1. reset to default state
        clearHitData();

        //2. update hit state
        updateHitState(event);

        //3. notify listener
        if (this.listener != null) {
            this.listener.onStart(this);
        }
    }

    private void handleActionMove(MotionEvent event) {
        //1. update hit state
        updateHitState(event);

        //2. update end point
        this.endX = event.getX();
        this.endY = event.getY();

        //3. notify listener if needed
        final int size = this.hitList.size();
        if ((this.listener != null) && (this.hitSize != size)) {
            this.hitSize = size;
            this.listener.onChange(this, this.hitList);
        }
    }

    private void handleActionUp(MotionEvent event) {
        //1. update hit state
        updateHitState(event);
        this.endX = 0;
        this.endY = 0;

        //2. notify listener
        if (this.listener != null) {
            this.listener.onComplete(this, this.hitList);
        }
    }

    private void updateHitState(MotionEvent event) {
        final float x = event.getX();
        final float y = event.getY();
        for (CellBean c : this.cellBeanList) {
            if (!c.isHit && c.of(x, y)) {
                c.isHit = true;
                this.hitList.add(c.id);
            }
        }
    }

    private void clearHitData() {
        this.resultState = ResultState.OK;
        for (int i = 0; i < this.hitList.size(); i++) {
            this.cellBeanList.get(hitList.get(i)).isHit = false;
        }
        this.hitList.clear();
        this.hitSize = 0;
    }
}
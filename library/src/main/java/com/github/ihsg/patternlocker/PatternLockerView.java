package com.github.ihsg.patternlocker;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hsg on 20/09/2017.
 */

public class PatternLockerView extends View {
    private static final String TAG = "PatternLockerView";

    private @ColorInt
    int normalColor;
    private @ColorInt
    int hitColor;
    private @ColorInt
    int errorColor;
    private @ColorInt
    int fillColor;
    private float lineWidth;
    private boolean enableAutoClean;

    private float endX;
    private float endY;

    private int hitSize;
    private boolean isError;
    private List<CellBean> cellBeanList;
    private List<Integer> hitList;
    private OnPatternChangeListener listener;

    private ILockerLinkedLineView linkedLineView;
    private INormalCellView normalCellView;
    private IHitCellView hitCellView;

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

    public void setOnPatternChangedListener(OnPatternChangeListener listener) {
        this.listener = listener;
    }

    public void updateStatus(boolean isError) {
        this.isError = isError;
        postInvalidate();
    }

    public void clearHitState() {
        clearHitData();
        this.isError = false;
        if (this.listener != null) {
            this.listener.onClear(this);
        }

        postInvalidate();
    }

    public int getNormalColor() {
        return normalColor;
    }

    public PatternLockerView setNormalColor(int normalColor) {
        this.normalColor = normalColor;
        return this;
    }

    public int getHitColor() {
        return hitColor;
    }

    public PatternLockerView setHitColor(int hitColor) {
        this.hitColor = hitColor;
        return this;
    }

    public int getErrorColor() {
        return errorColor;
    }

    public PatternLockerView setErrorColor(int errorColor) {
        this.errorColor = errorColor;
        return this;
    }

    public int getFillColor() {
        return fillColor;
    }

    public PatternLockerView setFillColor(int fillColor) {
        this.fillColor = fillColor;
        return this;
    }

    public float getLineWidth() {
        return lineWidth;
    }

    public PatternLockerView setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
        return this;
    }

    public ILockerLinkedLineView getLinkedLineView() {
        return linkedLineView;
    }

    public PatternLockerView setLinkedLineView(ILockerLinkedLineView linkedLineView) {
        this.linkedLineView = linkedLineView;
        return this;
    }

    public INormalCellView getNormalCellView() {
        return normalCellView;
    }

    public PatternLockerView setNormalCellView(INormalCellView normalCellView) {
        this.normalCellView = normalCellView;
        return this;
    }

    public IHitCellView getHitCellView() {
        return hitCellView;
    }

    public PatternLockerView setHitCellView(IHitCellView hitCellView) {
        this.hitCellView = hitCellView;
        return this;
    }

    public void buildWithDefaultStyle() {
        this.setNormalCellView(new DefaultLockerNormalCellView()
                .setNormalColor(this.getNormalColor())
                .setFillColor(this.getFillColor())
                .setLineWidth(this.getLineWidth())
        ).setHitCellView(new DefaultLockerHitCellView()
                .setHitColor(this.getHitColor())
                .setErrorColor(this.getErrorColor())
                .setFillColor(this.getFillColor())
                .setLineWidth(this.getLineWidth())
        ).setLinkedLineView(new DefaultLockerLinkedLineView()
                .setNormalColor(this.getHitColor())
                .setErrorColor(this.getErrorColor())
                .setLineWidth(this.getLineWidth())
        ).build();
    }

    public void build() {
        if (getNormalCellView() == null) {
            Log.e(TAG, "build(), normalCellView is null");
            return;
        }

        if (getHitCellView() == null) {
            Log.e(TAG, "build(), hitCellView is null");
            return;
        }

        if (getLinkedLineView() == null) {
            Log.w(TAG, "build(), linkedLineView is null");
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
            this.cellBeanList = new CellFactory(getWidth(), getHeight())
                    .getCellBeanList();
        }
        drawLinkedLine(canvas);
        drawCells(canvas);
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
        this.initAttrs(context, attrs, defStyleAttr);
        this.initData();
    }

    private void initAttrs(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        final TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PatternLockerView, defStyleAttr, 0);

        this.normalColor = ta.getColor(R.styleable.PatternLockerView_plv_color, Config.getDefaultNormalColor());
        this.hitColor = ta.getColor(R.styleable.PatternLockerView_plv_hitColor, Config.getDefaultHitColor());
        this.errorColor = ta.getColor(R.styleable.PatternLockerView_plv_errorColor, Config.getDefaultErrorColor());
        this.fillColor = ta.getColor(R.styleable.PatternLockerView_plv_fillColor, Config.getDefaultFillColor());
        this.lineWidth = ta.getDimension(R.styleable.PatternLockerView_plv_lineWidth, Config.getDefaultLineWidth(getResources()));
        this.enableAutoClean = ta.getBoolean(R.styleable.PatternLockerView_plv_enableAutoClean, Config.getDefaultEnableAutoClean());

        ta.recycle();

        this.setNormalColor(this.normalColor);
        this.setHitColor(this.hitColor);
        this.setErrorColor(this.errorColor);
        this.setFillColor(this.fillColor);
        this.setLineWidth(this.lineWidth);
    }

    private void initData() {
        this.hitList = new ArrayList<>();
        this.buildWithDefaultStyle();
    }

    private void drawLinkedLine(Canvas canvas) {
        if ((this.hitList != null) && !this.hitList.isEmpty() && (getLinkedLineView() != null)) {
            getLinkedLineView().draw(canvas,
                    this.hitList,
                    this.cellBeanList,
                    this.endX,
                    this.endY,
                    this.isError);
        }
    }

    private void drawCells(Canvas canvas) {
        if (getHitCellView() == null) {
            Log.e(TAG, "drawCells(), hitCellView is null");
            return;
        }

        if (getNormalCellView() == null) {
            Log.e(TAG, "drawCells(), normalCellView is null");
            return;
        }

        for (int i = 0; i < this.cellBeanList.size(); i++) {
            final CellBean item = this.cellBeanList.get(i);
            if (item.isHit) {
                getHitCellView().draw(canvas, item, this.isError);
            } else {
                getNormalCellView().draw(canvas, item);
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

        //3. startTimer if needed
        if (this.enableAutoClean && this.hitList.size() > 0) {
            startTimer();
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
        for (int i = 0; i < this.hitList.size(); i++) {
            this.cellBeanList.get(hitList.get(i)).isHit = false;
        }
        this.hitList.clear();
        this.hitSize = 0;
    }

    private final Runnable action = new Runnable() {
        @Override
        public void run() {
            setEnabled(true);
            clearHitState();
        }
    };

    @Override
    protected void onDetachedFromWindow() {
        this.setOnPatternChangedListener(null);
        this.removeCallbacks(this.action);
        super.onDetachedFromWindow();
    }

    private void startTimer() {
        setEnabled(false);
        this.postDelayed(this.action, Config.getDefaultDelayTime());
    }
}
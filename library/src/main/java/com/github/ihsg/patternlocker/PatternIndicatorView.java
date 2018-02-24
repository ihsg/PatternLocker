package com.github.ihsg.patternlocker;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hsg on 20/09/2017.
 */

public class PatternIndicatorView extends View {
    private static final String TAG = "PatternIndicatorView";

    private int normalColor;
    private int fillColor;
    private int hitColor;
    private int errorColor;
    private float lineWidth;

    private boolean isError;
    private List<Integer> hitList;
    private List<CellBean> cellBeanList;

    private IIndicatorLinkedLineView linkedLineView;
    private INormalCellView normalCellView;
    private IHitCellView hitCellView;


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

    public int getNormalColor() {
        return normalColor;
    }

    public PatternIndicatorView setNormalColor(int normalColor) {
        this.normalColor = normalColor;
        return this;
    }

    public int getFillColor() {
        return fillColor;
    }

    public PatternIndicatorView setFillColor(int fillColor) {
        this.fillColor = fillColor;
        return this;
    }

    public int getHitColor() {
        return hitColor;
    }

    public PatternIndicatorView setHitColor(int hitColor) {
        this.hitColor = hitColor;
        return this;
    }

    public int getErrorColor() {
        return errorColor;
    }

    public PatternIndicatorView setErrorColor(int errorColor) {
        this.errorColor = errorColor;
        return this;
    }

    public float getLineWidth() {
        return lineWidth;
    }

    public PatternIndicatorView setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
        return this;
    }

    public IIndicatorLinkedLineView getLinkedLineView() {
        return linkedLineView;
    }

    public PatternIndicatorView setLinkedLineView(IIndicatorLinkedLineView linkedLineView) {
        this.linkedLineView = linkedLineView;
        return this;
    }

    public INormalCellView getNormalCellView() {
        return normalCellView;
    }

    public PatternIndicatorView setNormalCellView(INormalCellView normalCellView) {
        this.normalCellView = normalCellView;
        return this;
    }

    public IHitCellView getHitCellView() {
        return hitCellView;
    }

    public PatternIndicatorView setHitCellView(IHitCellView hitCellView) {
        this.hitCellView = hitCellView;
        return this;
    }

    public void buildWithDefaultStyle() {
        this.setNormalCellView(new DefaultIndicatorNormalCellView()
                .setNormalColor(this.getNormalColor())
                .setFillColor(this.getFillColor())
                .setLineWidth(this.getLineWidth())
        ).setHitCellView(new DefaultIndicatorHitCellView()
                .setErrorColor(this.getErrorColor())
                .setNormalColor(this.getHitColor())
        ).setLinkedLineView(new DefaultIndicatorLinkedLineView()
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

    public void updateState(List<Integer> hitList, boolean isError) {
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
        this.isError = isError;

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
            this.cellBeanList = new CellFactory(getWidth() - getPaddingLeft() - getPaddingRight(),
                    getHeight() - getPaddingTop() - getPaddingBottom())
                    .getCellBeanList();
        }

        drawLinkedLine(canvas);
        drawCells(canvas);
    }

    private void init(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this.initAttrs(context, attrs, defStyleAttr);
        this.initData();
    }

    private void initAttrs(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        final TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PatternIndicatorView, defStyleAttr, 0);

        this.normalColor = ta.getColor(R.styleable.PatternIndicatorView_piv_color, Config.getDefaultNormalColor());
        this.fillColor = ta.getColor(R.styleable.PatternIndicatorView_piv_fillColor, Config.getDefaultFillColor());
        this.hitColor = ta.getColor(R.styleable.PatternIndicatorView_piv_hitColor, Config.getDefaultHitColor());
        this.errorColor = ta.getColor(R.styleable.PatternIndicatorView_piv_errorColor, Config.getDefaultErrorColor());
        this.lineWidth = ta.getDimension(R.styleable.PatternIndicatorView_piv_lineWidth, Config.getDefaultLineWidth(getResources()));

        ta.recycle();

        this.setNormalColor(this.normalColor);
        this.setFillColor(this.fillColor);
        this.setHitColor(this.hitColor);
        this.setErrorColor(this.errorColor);
        this.setLineWidth(this.lineWidth);
    }

    private void initData() {
        this.hitList = new ArrayList<>();
        this.buildWithDefaultStyle();
    }

    private void drawLinkedLine(Canvas canvas) {
        if (!this.hitList.isEmpty() && (this.getLinkedLineView() != null)) {
            this.getLinkedLineView().draw(canvas,
                    this.hitList,
                    this.cellBeanList,
                    this.isError);
        }
    }

    private void drawCells(Canvas canvas) {
        if (this.getHitCellView() == null) {
            Log.e(TAG, "drawCells(), hitCellView is null");
            return;
        }

        if (this.getNormalCellView() == null) {
            Log.e(TAG, "drawCells(), normalCellView is null");
            return;
        }

        for (int i = 0; i < this.cellBeanList.size(); i++) {
            CellBean item = this.cellBeanList.get(i);
            if (item.isHit) {
                this.getHitCellView().draw(canvas, item, this.isError);
            } else {
                this.getNormalCellView().draw(canvas, item);
            }
        }
    }
}
package com.github.ihsg.patternlocker;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;

/**
 * Created by hsg on 22/02/2018.
 */

public class DefaultIndicatorNormalCellView implements INormalCellView {

    private int normalColor;
    private int fillColor;
    private float lineWidth;

    private Paint paint;

    public DefaultIndicatorNormalCellView() {
        this.paint = Config.createPaint();
        this.paint.setStyle(Paint.Style.FILL);
    }

    public int getNormalColor() {
        return normalColor;
    }

    public DefaultIndicatorNormalCellView setNormalColor(int normalColor) {
        this.normalColor = normalColor;
        return this;
    }

    public int getFillColor() {
        return fillColor;
    }

    public DefaultIndicatorNormalCellView setFillColor(int fillColor) {
        this.fillColor = fillColor;
        return this;
    }

    public float getLineWidth() {
        return lineWidth;
    }

    public DefaultIndicatorNormalCellView setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
        return this;
    }

    @Override
    public void draw(@NonNull Canvas canvas, @NonNull CellBean cellBean) {
        int saveCount = canvas.save();

        //outer circle
        this.paint.setColor(this.getNormalColor());
        canvas.drawCircle(cellBean.x, cellBean.y, cellBean.radius, this.paint);

        //inner circle
        this.paint.setColor(this.getFillColor());
        canvas.drawCircle(cellBean.x, cellBean.y, cellBean.radius - this.getLineWidth(), this.paint);

        canvas.restoreToCount(saveCount);
    }
}
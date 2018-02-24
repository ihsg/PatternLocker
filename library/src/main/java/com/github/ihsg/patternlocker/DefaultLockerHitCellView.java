package com.github.ihsg.patternlocker;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;

/**
 * Created by hsg on 22/02/2018.
 */

public class DefaultLockerHitCellView implements IHitCellView {

    private @ColorInt
    int hitColor;
    private @ColorInt
    int errorColor;
    private @ColorInt
    int fillColor;
    private float lineWidth;

    private Paint paint;

    public DefaultLockerHitCellView() {
        this.paint = Config.createPaint();
        this.paint.setStyle(Paint.Style.FILL);
    }

    public @ColorInt
    int getHitColor() {
        return hitColor;
    }

    public DefaultLockerHitCellView setHitColor(@ColorInt int hitColor) {
        this.hitColor = hitColor;
        return this;
    }

    public @ColorInt
    int getErrorColor() {
        return errorColor;
    }

    public DefaultLockerHitCellView setErrorColor(@ColorInt int errorColor) {
        this.errorColor = errorColor;
        return this;
    }

    public @ColorInt
    int getFillColor() {
        return fillColor;
    }

    public DefaultLockerHitCellView setFillColor(@ColorInt int fillColor) {
        this.fillColor = fillColor;
        return this;
    }

    public float getLineWidth() {
        return lineWidth;
    }

    public DefaultLockerHitCellView setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
        return this;
    }

    @Override
    public void draw(@NonNull Canvas canvas, @NonNull CellBean cellBean, boolean isError) {
        final int saveCount = canvas.save();

        // draw outer circle
        this.paint.setColor(this.getColor(isError));
        canvas.drawCircle(cellBean.x, cellBean.y, cellBean.radius, this.paint);

        // draw fill circle
        this.paint.setColor(this.getFillColor());
        canvas.drawCircle(cellBean.x, cellBean.y, cellBean.radius - this.getLineWidth(), this.paint);

        // draw inner circle
        this.paint.setColor(this.getColor(isError));
        canvas.drawCircle(cellBean.x, cellBean.y, cellBean.radius / 5f, this.paint);

        canvas.restoreToCount(saveCount);
    }

    private @ColorInt
    int getColor(boolean isError) {
        return isError ? this.getErrorColor() : this.getHitColor();
    }
}

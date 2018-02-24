package com.github.ihsg.patternlocker;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;

/**
 * Created by hsg on 22/02/2018.
 */

public class DefaultIndicatorHitCellView implements IHitCellView {

    private @ColorInt
    int normalColor;
    private @ColorInt
    int errorColor;

    private Paint paint;

    public DefaultIndicatorHitCellView() {
        this.paint = Config.createPaint();
        this.paint.setStyle(Paint.Style.FILL);
    }

    public int getNormalColor() {
        return normalColor;
    }

    public DefaultIndicatorHitCellView setNormalColor(int normalColor) {
        this.normalColor = normalColor;
        return this;
    }

    public int getErrorColor() {
        return errorColor;
    }

    public DefaultIndicatorHitCellView setErrorColor(int errorColor) {
        this.errorColor = errorColor;
        return this;
    }

    @Override
    public void draw(@NonNull Canvas canvas, @NonNull CellBean cellBean, boolean isError) {
        int saveCount = canvas.save();

        this.paint.setColor(this.getColor(isError));
        canvas.drawCircle(cellBean.x, cellBean.y, cellBean.radius, this.paint);

        canvas.restoreToCount(saveCount);
    }

    private int getColor(boolean isError) {
        return isError ? this.getErrorColor() : this.getNormalColor();
    }
}
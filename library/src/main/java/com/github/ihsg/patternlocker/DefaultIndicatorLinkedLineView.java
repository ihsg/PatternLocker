package com.github.ihsg.patternlocker;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

/**
 * Created by hsg on 22/02/2018.
 */

public class DefaultIndicatorLinkedLineView implements IIndicatorLinkedLineView {
    private @ColorInt
    int normalColor;
    private @ColorInt
    int errorColor;
    private float lineWidth;

    private Paint paint;

    public DefaultIndicatorLinkedLineView() {
        this.paint = Config.createPaint();
        this.paint.setStyle(Paint.Style.STROKE);
    }

    public int getNormalColor() {
        return normalColor;
    }

    public DefaultIndicatorLinkedLineView setNormalColor(int normalColor) {
        this.normalColor = normalColor;
        return this;
    }

    public int getErrorColor() {
        return errorColor;
    }

    public DefaultIndicatorLinkedLineView setErrorColor(int errorColor) {
        this.errorColor = errorColor;
        return this;
    }

    public float getLineWidth() {
        return lineWidth;
    }

    public DefaultIndicatorLinkedLineView setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
        return this;
    }

    @Override
    public void draw(@NonNull Canvas canvas, @Nullable List<Integer> hitList, @NonNull List<CellBean> cellBeanList, boolean isError) {
        if (hitList == null || hitList.isEmpty() || cellBeanList.isEmpty()) {
            return;
        }

        final int saveCount = canvas.save();

        final CellBean first = cellBeanList.get(hitList.get(0));
        final Path path = new Path();
        path.moveTo(first.x, first.y);

        for (int i = 1; i < hitList.size(); i++) {
            CellBean c = cellBeanList.get(hitList.get(i));
            path.lineTo(c.x, c.y);
        }

        this.paint.setColor(this.getColor(isError));
        this.paint.setStrokeWidth(this.getLineWidth());
        canvas.drawPath(path, this.paint);

        canvas.restoreToCount(saveCount);
    }

    private int getColor(boolean isError) {
        return isError ? this.getErrorColor() : this.getNormalColor();
    }
}

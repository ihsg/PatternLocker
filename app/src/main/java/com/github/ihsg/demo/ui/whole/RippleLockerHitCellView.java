package com.github.ihsg.demo.ui.whole;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;

import com.github.ihsg.patternlocker.CellBean;
import com.github.ihsg.patternlocker.IHitCellView;

/**
 * Created by hsg on 24/02/2018.
 */

public class RippleLockerHitCellView implements IHitCellView {

    private @ColorInt
    int hitColor;
    private @ColorInt
    int errorColor;

    private Paint paint;

    public RippleLockerHitCellView() {
        this.paint = new Paint();
        paint.setDither(true);
        paint.setAntiAlias(true);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        this.paint.setStyle(Paint.Style.FILL);
    }

    public @ColorInt
    int getHitColor() {
        return hitColor;
    }

    public RippleLockerHitCellView setHitColor(@ColorInt int hitColor) {
        this.hitColor = hitColor;
        return this;
    }

    public @ColorInt
    int getErrorColor() {
        return errorColor;
    }

    public RippleLockerHitCellView setErrorColor(@ColorInt int errorColor) {
        this.errorColor = errorColor;
        return this;
    }

    @Override
    public void draw(@NonNull Canvas canvas, @NonNull CellBean cellBean, boolean isError) {
        final int saveCount = canvas.save();

        this.paint.setColor(getColor(isError) & 0x14FFFFFF);
        canvas.drawCircle(cellBean.x, cellBean.y, cellBean.radius, this.paint);

        this.paint.setColor(getColor(isError) & 0x43FFFFFF);
        canvas.drawCircle(cellBean.x, cellBean.y, cellBean.radius * 2f / 3f, this.paint);

        this.paint.setColor(getColor(isError));
        canvas.drawCircle(cellBean.x, cellBean.y, cellBean.radius / 3f, this.paint);

        canvas.restoreToCount(saveCount);
    }

    private @ColorInt
    int getColor(boolean isError) {
        return isError ? this.getErrorColor() : this.getHitColor();
    }
}

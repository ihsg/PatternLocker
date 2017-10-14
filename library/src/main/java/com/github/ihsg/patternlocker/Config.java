package com.github.ihsg.patternlocker;

import android.content.res.Resources;
import android.graphics.Color;
import android.util.TypedValue;

/**
 * Created by hsg on 23/09/2017.
 */
class Config {
    private static final String DEFAULT_COLOR = "#2196F3";
    private static final String DEFAULT_HIT_COLOR = "#3F51B5";
    private static final String DEFAULT_ERROR_COLOR = "#F44336";
    private static final String DEFAULT_FILL_COLOR = "#FAFAFA";
    private static final int LINE_WIDTH = 1;

    public static int getDefaultColor() {
        return Color.parseColor(DEFAULT_COLOR);
    }

    public static int getHitColor() {
        return Color.parseColor(DEFAULT_HIT_COLOR);
    }

    public static int getErrorColor() {
        return Color.parseColor(DEFAULT_ERROR_COLOR);
    }

    public static int getFillColor() {
        return Color.parseColor(DEFAULT_FILL_COLOR);
    }

    public static int getColorByState(ResultState resultState) {
        return resultState == ResultState.OK ? getHitColor() : getErrorColor();
    }

    public static float getLineWidth(Resources resources) {
        return convertPx2Dp(LINE_WIDTH, resources);
    }

    private static float convertPx2Dp(float px, Resources resources) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, resources.getDisplayMetrics());
    }
}
package com.github.ihsg.patternlocker;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

/**
 * Created by hsg on 22/02/2018.
 */

public interface ILockerLinkedLineView {
    /**
     * 绘制图案密码连接线
     *
     * @param canvas
     * @param hitList
     * @param cellBeanList
     * @param endX
     * @param endY
     * @param isError
     */
    void draw(@NonNull Canvas canvas,
              @Nullable List<Integer> hitList,
              @NonNull List<CellBean> cellBeanList,
              float endX,
              float endY,
              boolean isError);
}
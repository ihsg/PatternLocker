package com.github.ihsg.patternlocker;

import java.util.List;

/**
 * Created by hsg on 14/10/2017.
 */

public interface OnPatternChangeListener {
    /**
     * 图案绘制开始会回调自方法
     *
     * @param view
     */
    void onStart(PatternLockerView view);

    /**
     * 图案绘制改变会回调自方法，只有@param hitList 改变了才会触发此方法
     *
     * @param view
     * @param hitList
     */
    void onChange(PatternLockerView view, List<Integer> hitList);

    /**
     * 图案绘制完成会回调自方法
     *
     * @param view
     * @param hitList
     */
    void onComplete(PatternLockerView view, List<Integer> hitList);

    /**
     * 已绘制的图案被清除会回调自方法
     *
     * @param view
     */
    void onClear(PatternLockerView view);
}
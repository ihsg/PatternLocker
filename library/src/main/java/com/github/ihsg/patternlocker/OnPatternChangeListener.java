package com.github.ihsg.patternlocker;

import java.util.List;

/**
 * Created by hsg on 14/10/2017.
 */

public interface OnPatternChangeListener {
    void onStart(PatternLockerView view);

    void onChange(PatternLockerView view, List<Integer> hitList);

    void onComplete(PatternLockerView view, List<Integer> hitList);

    void onClear(PatternLockerView view);
}

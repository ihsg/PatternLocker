package com.github.ihsg.patternlocker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hsg on 23/09/2017.
 */

public class ManagerCenter {
    private static ManagerCenter instance;
    private List<Integer> hitList;
    private ResultState resultState;
    private ChangeListener changeListener;

    interface ChangeListener {
        void onChanged();
    }

    private ManagerCenter() {
        this.hitList = new ArrayList<>();
        this.resultState = ResultState.OK;
    }

    public static ManagerCenter getInstance() {
        if (instance == null) {
            instance = new ManagerCenter();
        }
        return instance;
    }

    public List<Integer> getHitList() {
        return hitList;
    }

    public void addHit(int id) {
        this.hitList.add(id);
        noteChange();
    }

    public void clearHit() {
        this.hitList.clear();
        noteChange();
    }

    public void setResultState(ResultState resultState) {
        this.resultState = resultState;
        noteChange();
    }

    public ResultState getResultState() {
        return resultState;
    }

    public void setChangeListener(ChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    public void checkResult() {
        if (this.hitList.size() < 4) {
            setResultState(ResultState.ERROR);
        }
    }

    private void noteChange() {
        if (this.changeListener != null) {
            this.changeListener.onChanged();
        }
    }
}
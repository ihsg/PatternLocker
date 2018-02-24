package com.github.ihsg.demo.util;

import android.text.TextUtils;

import java.util.List;

/**
 * Created by hsg on 14/10/2017.
 */

public class PatternHelper {
    public static final int MAX_SIZE = 4;
    public static final int MAX_TIMES = 5;
    private static final String GESTURE_PWD_KEY = "gesture_pwd_key";

    private String message;
    private String storagePwd;
    private String tmpPwd;
    private int times;
    private boolean isFinish;
    private boolean isOk;

    public void validateForSetting(List<Integer> hitList) {
        this.isFinish = false;
        this.isOk = false;

        if ((hitList == null) || (hitList.size() < MAX_SIZE)) {
            this.tmpPwd = null;
            this.message = getSizeErrorMsg();
            return;
        }

        //1. draw first time
        if (TextUtils.isEmpty(this.tmpPwd)) {
            this.tmpPwd = convert2String(hitList);
            this.message = getReDrawMsg();
            this.isOk = true;
            return;
        }

        //2. draw second times
        if (this.tmpPwd.equals(convert2String(hitList))) {
            this.message = getSettingSuccessMsg();
            saveToStorage(this.tmpPwd);
            this.isOk = true;
            this.isFinish = true;
        } else {
            this.tmpPwd = null;
            this.message = getDiffPreErrorMsg();
        }
    }

    public void validateForChecking(List<Integer> hitList) {
        this.isOk = false;

        if ((hitList == null) || (hitList.size() < MAX_SIZE)) {
            this.times++;
            this.isFinish = this.times >= MAX_SIZE;
            this.message = getPwdErrorMsg();
            return;
        }

        this.storagePwd = getFromStorage();
        if (!TextUtils.isEmpty(this.storagePwd) && this.storagePwd.equals(convert2String(hitList))) {
            this.message = getCheckingSuccessMsg();
            this.isOk = true;
            this.isFinish = true;
        } else {
            this.times++;
            this.isFinish = this.times >= MAX_SIZE;
            this.message = getPwdErrorMsg();
        }
    }

    public String getMessage() {
        return this.message;
    }

    public boolean isFinish() {
        return isFinish;
    }

    public boolean isOk() {
        return isOk;
    }

    private String getReDrawMsg() {
        return "请再次绘制解锁图案";
    }

    private String getSettingSuccessMsg() {
        return "手势解锁图案设置成功！";
    }

    private String getCheckingSuccessMsg() {
        return "解锁成功！";
    }

    private String getSizeErrorMsg() {
        return String.format("至少连接个%d点，请重新绘制", MAX_SIZE);
    }

    private String getDiffPreErrorMsg() {
        return "与上次绘制不一致，请重新绘制";
    }

    private String getPwdErrorMsg() {
        return String.format("密码错误，还剩%d次机会", getRemainTimes());
    }

    private String convert2String(List<Integer> hitList) {
        return hitList.toString();
    }

    private void saveToStorage(String gesturePwd) {
        final String encryptPwd = SecurityUtil.encrypt(gesturePwd);
        SharedPreferencesUtil.getInstance().saveString(GESTURE_PWD_KEY, encryptPwd);
    }

    private String getFromStorage() {
        final String result = SharedPreferencesUtil.getInstance().getString(GESTURE_PWD_KEY);
        return SecurityUtil.decrypt(result);
    }

    private int getRemainTimes() {
        return (times < 5) ? (MAX_TIMES - times) : 0;
    }
}

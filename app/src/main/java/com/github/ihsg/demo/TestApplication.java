package com.github.ihsg.demo;

import android.app.Application;
import android.content.Context;

/**
 * Created by hsg on 14/10/2017.
 */

public class TestApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}

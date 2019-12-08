package com.tbright.sketchpad;

import android.app.Application;

public class BaseApplication extends Application {
    public static Application instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}

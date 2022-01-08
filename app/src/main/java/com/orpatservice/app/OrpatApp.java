package com.orpatservice.app;

import android.app.Application;

import com.orpatservice.app.data.sharedprefs.SharedPrefs;

public class OrpatApp extends Application {
    public static OrpatApp applicationInstance;

    public static OrpatApp getInstance() {
        return applicationInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPrefs.initialize(this);

        applicationInstance = this;
    }
}

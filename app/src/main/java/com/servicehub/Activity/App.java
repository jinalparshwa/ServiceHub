package com.servicehub.Activity;

import android.support.multidex.MultiDexApplication;

/**
 * Created by admin on 1/30/2017.
 */

 public class  App extends MultiDexApplication {
    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }
}
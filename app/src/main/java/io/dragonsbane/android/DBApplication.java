package io.dragonsbane.android;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import io.onemfive.android.api.service.OneMFiveAndroidRouterService;

/**
 * TODO: Add Definition
 * @author objectorange
 */
public class DBApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Intent i = new Intent(this, OneMFiveAndroidRouterService.class);
        Log.i(DBApplication.class.getName(),"Starting OneMFiveAndroidRouterService to bootstrap 1M5 Core...");
        startService(i);
    }
}

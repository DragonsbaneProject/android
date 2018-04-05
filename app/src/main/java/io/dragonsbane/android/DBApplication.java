package io.dragonsbane.android;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import io.synapticcelerity.android.api.service.SCAndroidRouterService;

/**
 * TODO: Add Definition
 * @author objectorange
 */
public class DBApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Intent i = new Intent(this, SCAndroidRouterService.class);
        Log.i(DBApplication.class.getName(),"Starting SCAndroidRouteService to bootstrap SC Core...");
        startService(i);
    }
}

package io.dragonsbane.neurocog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import io.dragonsbane.data.ImpairmentTest;
import io.onemfive.android.api.service.OneMFiveAndroidRouterService;
import io.onemfive.android.api.util.AndroidHelper;
import io.onemfive.data.Envelope;
import io.onemfive.data.util.JSONParser;

public class ServiceAPI extends OneMFiveAndroidRouterService {

    private static DBApplication app;

    public ServiceAPI() {
        super(ServiceAPI.class.getName());
        Properties p = new Properties();
        p.put(OneMFiveAndroidRouterService.PARAM_ROOT_DIR,"dgb");
        super.properties = p;
    }

    public static void setApp(DBApplication application) {
        app = application;
    }

    public static Boolean saveTest(ImpairmentTest test) {
        Log.i(ServiceAPI.class.getSimpleName(),"Saving test...");
        SharedPreferences pref = app.getApplicationContext().getSharedPreferences(ImpairmentTest.class.getName(), MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(test.getId(), JSONParser.toString(test.toMap()));
        editor.apply();
        Log.i(ServiceAPI.class.getName(),"Test saved.");
        return true;
    }

    public static List<ImpairmentTest> loadTests() {
        Log.i(ServiceAPI.class.getSimpleName(),"Saving test...");
        List<ImpairmentTest> tests = new ArrayList<>();
        SharedPreferences pref = app.getApplicationContext().getSharedPreferences(ImpairmentTest.class.getName(), MODE_PRIVATE);
        Map testMaps = pref.getAll();
        Collection<String> testJSONs = testMaps.values();
        ImpairmentTest t;
        for(String ts : testJSONs) {
            t = new ImpairmentTest();
            t.fromMap((Map<String,Object>)JSONParser.parse(ts));
            tests.add(t);
        }
        Log.i(ServiceAPI.class.getName(),"Tests loaded.");
        return tests;
    }

    private static void send(Context ctx, Envelope e) {
        Intent i = new Intent(ctx, ServiceAPI.class);
        AndroidHelper.setEnvelope(i,e);
        Log.i(ServiceAPI.class.getName(),"Sending request to ServiceAPI");
        ctx.startService(i);
    }
}

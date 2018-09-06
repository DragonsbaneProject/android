package io.dragonsbane.neurocog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import io.dragonsbane.data.ImpairmentTest;
import io.onemfive.android.api.db.AndroidFSInfoVaultDB;
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
        try {
            app.getDb().save(ImpairmentTest.class.getName(), test.getId(), JSONParser.toString(test.toMap()).getBytes(), true);
        } catch (FileNotFoundException e) {
            Log.w(ServiceAPI.class.getName(), "FileNotFoundException caught attempting to save an ImpairmentTest. ");
        }
        Log.i(ServiceAPI.class.getName(),"Test saved.");
        return true;
    }

    public static List<ImpairmentTest> loadTests() {
        Log.i(ServiceAPI.class.getSimpleName(),"Loading all tests...");
        List<ImpairmentTest> tests = new ArrayList<>();
        List<byte[]> testByteList = app.getDb().loadAll(ImpairmentTest.class.getName());
        ImpairmentTest t;
        for(byte[] ts : testByteList) {
            t = new ImpairmentTest();
            t.fromMap((Map<String,Object>)JSONParser.parse(new String(ts)));
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

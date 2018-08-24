package io.dragonsbane.android.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.dragonsbane.android.tests.ImpairmentTest;
import io.onemfive.android.api.util.AndroidHelper;
import io.onemfive.core.infovault.InfoVaultService;
import io.onemfive.data.DID;
import io.onemfive.data.Envelope;
import io.onemfive.data.util.DLC;

public class ServiceAPI {

    public static final String MEMORY_TESTS_LOADED = "io.dragonsbane.android.service.MemoryTests.Loaded";

    private static DID userDID = null;

    public static void setUserDID(DID did) { userDID = did; }

    public static void saveTest(Context ctx, ImpairmentTest test) {
        Log.i(ServiceAPI.class.getSimpleName(),"Saving test...");
        Envelope e = Envelope.documentFactory();
        e.setDID(userDID);
        DLC.addEntity(test,e);
        DLC.addRoute(InfoVaultService.class, InfoVaultService.OPERATION_EXECUTE,e);
        send(ctx, e);
        Log.i(ServiceAPI.class.getSimpleName(),"Test saved.");
    }

    public static void saveTests(Context ctx, List<ImpairmentTest> tests) {
        Envelope e = Envelope.documentFactory();
        e.setDID(userDID);
        DLC.addEntity(tests,e);
        DLC.addRoute(InfoVaultService.class, InfoVaultService.OPERATION_EXECUTE,e);
        send(ctx, e);
    }

    public static void loadTests(Context ctx) {
        Envelope e = Envelope.documentFactory();
        e.setClientReplyAction(MEMORY_TESTS_LOADED);
        e.setDID(userDID);
        List<ImpairmentTest> tests = new ArrayList<>();
        ImpairmentTest test = new ImpairmentTest();
        tests.add(test);
        DLC.addEntity(tests,e);
        DLC.addRoute(InfoVaultService.class, InfoVaultService.OPERATION_EXECUTE,e);
        send(ctx, e);
    }

    private static void send(Context ctx, Envelope e) {
        Intent i = new Intent(ctx, DragonsbaneAndroidService.class);
        AndroidHelper.setEnvelope(i,e);
        Log.i(ServiceAPI.class.getName(),"Sending request to DragonsbaneAndroidService");
        ctx.startService(i);
    }
}

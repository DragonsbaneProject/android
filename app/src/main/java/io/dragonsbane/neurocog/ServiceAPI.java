package io.dragonsbane.neurocog;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.List;
import java.util.Properties;

import io.dragonsbane.data.ImpairmentTest;
import io.dragonsbane.neurocog.database.LoadImpairmentTestsDAO;
import io.dragonsbane.neurocog.database.SaveImpairmentTestDAO;
import io.onemfive.android.api.service.OneMFiveAndroidRouterService;
import io.onemfive.android.api.util.AndroidHelper;
import io.onemfive.data.Envelope;

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

    public static Boolean saveTest(Context ctx, ImpairmentTest test) {
        Log.i(ServiceAPI.class.getSimpleName(),"Saving test...");
        SaveImpairmentTestDAO dao = new SaveImpairmentTestDAO(app.getDb(), test);
        try {
            dao.execute();
        } catch (Exception e1) {
            Log.w(ServiceAPI.class.getName(),e1.getLocalizedMessage());
            return false;
        }
        Log.i(ServiceAPI.class.getName(),"Test saved.");
        return true;
    }

    public static List<ImpairmentTest> loadTests(Context ctx) {
        LoadImpairmentTestsDAO dao = new LoadImpairmentTestsDAO(app.getDb(), app.getDid());
        try {
            dao.execute();
        } catch (Exception e) {
            Log.w(ServiceAPI.class.getName(),e.getLocalizedMessage());
        }
        return dao.getTests();
    }

    private static void send(Context ctx, Envelope e) {
        Intent i = new Intent(ctx, ServiceAPI.class);
        AndroidHelper.setEnvelope(i,e);
        Log.i(ServiceAPI.class.getName(),"Sending request to ServiceAPI");
        ctx.startService(i);
    }
}

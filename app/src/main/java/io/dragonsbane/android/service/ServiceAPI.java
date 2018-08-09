package io.dragonsbane.android.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import io.onemfive.android.api.util.AndroidHelper;
import io.onemfive.data.DID;
import io.onemfive.data.Envelope;

public class ServiceAPI {

    private static DID userDID = null;

    public static void setUserDID(DID did) { userDID = did; }



    private static void send(Context ctx, Envelope e) {
        Intent i = new Intent(ctx, DragonsbaneAndroidService.class);
        AndroidHelper.setEnvelope(i,e);
        Log.i(ServiceAPI.class.getName(),"Sending request to DragonsbaneAndroidService");
        ctx.startService(i);
    }
}

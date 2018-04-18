package io.dragonsbane.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import io.onemfive.android.api.SecurityAPI;
import io.onemfive.android.api.healthcare.HealthRecordAPI;
import io.onemfive.data.DocumentMessage;
import io.onemfive.data.Envelope;
import io.onemfive.data.HealthRecord;
import io.onemfive.data.LID;

/**
 * TODO: Add Definition
 * @author objectorange
 */
public class MainActivity extends AppCompatActivity {

    private BroadcastReceiver createLIDReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(MainActivity.class.getSimpleName(),"Received broadcast from LID creation.");
            Envelope e = (Envelope)intent.getExtras().get(Envelope.class.getName());
            LID lid = (LID)((DocumentMessage)e.getMessage()).data.get(LID.class.getName());
            if(lid.getStatus() == LID.Status.ACTIVE)
//                navToHome(e);
                System.out.println("LID created.");
            else
//                showError(e);
                System.out.println("LID not created.");
        }
    };

    private BroadcastReceiver authenticateLIDReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(MainActivity.class.getSimpleName(),"Received broadcast from LID authN.");
            Envelope e = (Envelope)intent.getExtras().get(Envelope.class.getName());
            LID lid = (LID)((DocumentMessage)e.getMessage()).data.get(LID.class.getName());
            if(lid.getAuthenticated())
//                navToHome(e);
                System.out.println("LID authenticated.");
            else
//                showError(e);
                System.out.println("LID not authenticated.");
        }
    };

    private BroadcastReceiver loadHealthRecordReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(MainActivity.class.getSimpleName(),"Received broadcast from Health Record loading.");
            Envelope e = (Envelope)intent.getExtras().get(Envelope.class.getName());
            HealthRecord record = (HealthRecord) ((DocumentMessage)e.getMessage()).data.get(LID.class.getName());
            if(record != null)
//                navToHome(e);
                System.out.println("Health Record loaded.");
            else
//                showError(e);
                System.out.println("Health Record not found.");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerReceiver(createLIDReceiver, new IntentFilter(SecurityAPI.LIDCreated));
        registerReceiver(authenticateLIDReceiver, new IntentFilter(SecurityAPI.LIDAuthenticated));
        registerReceiver(loadHealthRecordReceiver, new IntentFilter(HealthRecordAPI.HealthRecordLoaded));
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(createLIDReceiver);
        unregisterReceiver(authenticateLIDReceiver);
        unregisterReceiver(loadHealthRecordReceiver);
    }

    public void createLID(View view) {
        Log.i(MainActivity.class.getName(),"Creating LID");
        LID lid = new LID();
        lid.setAlias("Alice");
        lid.setPassphrase("1234");
        SecurityAPI.createLID(this, lid);
    }

    public void authenticateLID(View view) {
        Log.i(MainActivity.class.getName(),"Authenticating LID");
        LID lid = new LID();
        lid.setAlias("Alice");
        lid.setPassphrase("1234");
        SecurityAPI.authenticateLID(this, lid);
    }

    public void loadHealthRecord(View view) {
        Log.i(MainActivity.class.getName(),"Loading Health Record");
        HealthRecord record = new HealthRecord();
        LID lid = new LID();
        lid.setAlias("Alice");
        lid.setPassphrase("1234");
        HealthRecordAPI.loadHealthRecord(this, record);
    }

    private void showError(Envelope e) {

    }

    private void navToHome(Envelope e) {

    }
}

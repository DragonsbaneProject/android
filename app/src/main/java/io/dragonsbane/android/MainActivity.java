package io.dragonsbane.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import io.synapticcelerity.android.api.core.SecurityAPI;
import io.synapticcelerity.data.DocumentMessage;
import io.synapticcelerity.data.Envelope;
import io.synapticcelerity.data.LID;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerReceiver(createLIDReceiver, new IntentFilter(SecurityAPI.LIDCreated));
        registerReceiver(authenticateLIDReceiver, new IntentFilter(SecurityAPI.LIDAuthenticated));
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(createLIDReceiver);
        unregisterReceiver(authenticateLIDReceiver);
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

    private void showError(Envelope e) {

    }

    private void navToHome(Envelope e) {

    }
}

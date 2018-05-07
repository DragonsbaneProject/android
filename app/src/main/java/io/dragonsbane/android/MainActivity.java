package io.dragonsbane.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

import io.dragonsbane.android.neurocog.PreTestActivity;
import io.onemfive.android.api.SecurityAPI;
import io.onemfive.android.api.healthcare.HealthRecordAPI;
import io.onemfive.core.infovault.InfoVaultService;
import io.onemfive.data.DID;
import io.onemfive.data.DocumentMessage;
import io.onemfive.data.Envelope;
import io.onemfive.data.health.HealthRecord;

/**
 * TODO: Add Definition
 * @author objectorange
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((DBApplication)getApplication()).addActivity(MainActivity.class, this);
        registerReceiver(verifyDIDReceiver, new IntentFilter(SecurityAPI.DIDVerified));
        registerReceiver(createDIDReceiver, new IntentFilter(SecurityAPI.DIDCreated));
        registerReceiver(authenticateDIDReceiver, new IntentFilter(SecurityAPI.DIDAuthenticated));
        registerReceiver(loadHealthRecordReceiver, new IntentFilter(HealthRecordAPI.HealthRecordLoaded));
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(verifyDIDReceiver);
        unregisterReceiver(createDIDReceiver);
        unregisterReceiver(authenticateDIDReceiver);
        unregisterReceiver(loadHealthRecordReceiver);
        ((DBApplication)getApplication()).removeActivity(MainActivity.class);
    }

    public void verifyDID(View view) {
        Log.i(MainActivity.class.getName(),"Verifying DID...");

        String username = ((EditText)findViewById(R.id.mainEditUsername)).getText().toString();
        if("".equals(username)) {
            showError(getResources().getText(R.string.usernameRequired).toString());
            return;
        }
        String password = ((EditText)findViewById(R.id.mainEditPassword)).getText().toString();
        if("".equals(password)) {
            showError(getResources().getText(R.string.passwordRequired).toString());
            return;
        }

        DID did = new DID();
        did.setAlias(username);
        did.setPassphrase(password);
        SecurityAPI.verifyDID(this, did);
    }

    private void createDID(DID did) {
        Log.i(MainActivity.class.getName(),"Creating DID...");
        SecurityAPI.createDID(this, did);
    }

    private void authenticateDID(DID did) {
        Log.i(MainActivity.class.getName(),"Authenticating DID...");
        SecurityAPI.authenticateDID(this, did);
    }

    private void loadHealthRecord(DID did) {
        Log.i(MainActivity.class.getName(),"Loading Health Record...");
        HealthRecordAPI.loadHealthRecord(this, did);
    }

    private void showError(String error) {
        showMessage(error, getResources().getColor(R.color.colorWarning));
    }

    private void showMessage(String message, int color) {
        TextView messageView = findViewById(R.id.mainTextMessage);
        messageView.setTextColor(color);
        messageView.setText(message);
        messageView.setVisibility(View.VISIBLE);
    }

    private void startTest() {
        boolean baseline = ((CheckBox)findViewById(R.id.mainBaseline)).isChecked();
        String bacStr = ((EditText)findViewById(R.id.mainEditBAC)).getText().toString();
        double bac = 0.0D;
        if(bacStr != null) {
            bac = Double.parseDouble(bacStr);
        }
        TextView messageView = findViewById(R.id.mainTextMessage);
        messageView.setVisibility(View.INVISIBLE);

        Intent intent = new Intent(this, PreTestActivity.class);
        startActivity(intent);
    }

    public void clearProfile(View view) {
        String username = ((EditText)findViewById(R.id.mainEditUsername)).getText().toString();
        if("".equals(username)) {
            showError(getResources().getText(R.string.usernameRequired).toString());
        } else {
            ((DBApplication)getApplication()).setDid(null);
            ((EditText)findViewById(R.id.mainEditUsername)).setText("");
            ((EditText)findViewById(R.id.mainEditPassword)).setText("");
        }
    }

    private BroadcastReceiver verifyDIDReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(MainActivity.class.getSimpleName(),"Received broadcast from DID verification.");
            Envelope e = (Envelope)intent.getExtras().get(Envelope.class.getName());
            DID did = (DID)e.getHeader(Envelope.DID);
            if(!did.getVerified()) {
                System.out.println("DID not registered.");
                createDID(did);
            } else {
                System.out.println("DID registered.");
                authenticateDID(did);
            }
        }
    };

    private BroadcastReceiver createDIDReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(MainActivity.class.getSimpleName(),"Received broadcast from DID creation.");
            Envelope e = (Envelope)intent.getExtras().get(Envelope.class.getName());
            DID did = (DID)e.getHeader(Envelope.DID);
            if(did.getStatus() == DID.Status.ACTIVE) {
                System.out.println("DID created. Load Health Record...");
                loadHealthRecord(did);
            } else {
                showError("Error creating DID.");
            }
        }
    };

    private BroadcastReceiver authenticateDIDReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(MainActivity.class.getSimpleName(),"Received broadcast from DID authN.");
            Envelope e = (Envelope)intent.getExtras().get(Envelope.class.getName());
            DID did = (DID)e.getHeader(Envelope.DID);
            if(did.getAuthenticated()) {
                System.out.println("DID authenticated. Load Health Record...");
                loadHealthRecord(did);
            } else {
                System.out.println("DID not authenticated.");
                showError(getResources().getText(R.string.passwordFailed).toString());
            }
        }
    };

    private BroadcastReceiver loadHealthRecordReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(MainActivity.class.getSimpleName(),"Received broadcast from Health Record loading.");
            Envelope e = (Envelope)intent.getExtras().get(Envelope.class.getName());
            DocumentMessage m = (DocumentMessage)e.getMessage();
            HealthRecord healthRecord = (HealthRecord)m.data.get(0).get(InfoVaultService.ENTITY);
            if(healthRecord != null) {
                System.out.println("Health Record loaded; healthStatus="+healthRecord.getHealthStatus().name());
                ((DBApplication)getApplication()).setHealthRecord(healthRecord);
                startTest();
            } else {
                System.out.println("Health Record not found.");
                showError("Error loading Health Record.");
            }
        }
    };
}

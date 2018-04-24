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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.dragonsbane.android.neurocog.persistence.Storage;
import io.dragonsbane.android.neurocog.ui.PreTestActivity;
import io.onemfive.android.api.SecurityAPI;
import io.onemfive.android.api.healthcare.HealthRecordAPI;
import io.onemfive.data.DID;
import io.onemfive.data.DocumentMessage;
import io.onemfive.data.Envelope;
import io.onemfive.data.TestReport;

/**
 * TODO: Add Definition
 * @author objectorange
 */
public class MainActivity extends AppCompatActivity {

    private BroadcastReceiver verifyDIDReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(MainActivity.class.getSimpleName(),"Received broadcast from DID verification.");
            Envelope e = (Envelope)intent.getExtras().get(Envelope.class.getName());
            DID did = (DID)e.getHeader(Envelope.DID);
            if(did.getStatus() == DID.Status.UNREGISTERED) {
                System.out.println("DID not registered.");
                createLID(did);
            } else {
                System.out.println("DID registered.");
                authenticateLID(did);
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
                System.out.println("DID created.");
                try {
                    Storage.writeInternalObject(context, did.getAlias(), did);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
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
                System.out.println("DID authenticated.");
                try {
                    Storage.writeInternalObject(context, did.getAlias(), did);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
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
            String healthStatus = (String) m.data.get("healthStatus");
            if(healthStatus != null) {
                System.out.println("Health Record loaded; healthStatus="+healthStatus);
                ((DBApplication)getApplication()).setHealthRecord(m.data);
                startTest(null);
            } else {
                System.out.println("Health Record not found.");
                showError("Error loading Health Record.");
            }
        }
    };

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

    public void verifyLID(View view) {
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
        SecurityAPI.verifyLID(this, did);
    }

    private void createLID(DID did) {
        Log.i(MainActivity.class.getName(),"Creating DID...");
        SecurityAPI.createLID(this, did);
    }

    private void authenticateLID(DID did) {
        Log.i(MainActivity.class.getName(),"Authenticating DID...");
        SecurityAPI.authenticateLID(this, did);
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

    private void startTest(View view) {
        TestReport report = new TestReport();
        ((DBApplication)getApplicationContext()).setTestReport(report);
        boolean baseline = ((CheckBox)findViewById(R.id.mainBaseline)).isChecked();
        report.setBaseline(baseline);
        report.setStart(new Date());

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
            try {
                Storage.writeInternalObject(this, username, null);
                showMessage(getResources().getText(R.string.profileCleared).toString(), getResources().getColor(R.color.colorPrimaryDark));
            } catch (IOException e) {
                e.printStackTrace();
                showError(e.getLocalizedMessage());
            }
        }
    }
}

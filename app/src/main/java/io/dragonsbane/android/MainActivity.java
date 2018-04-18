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

import io.dragonsbane.android.neurocog.persistence.Storage;
import io.dragonsbane.android.neurocog.ui.PreTestActivity;
import io.onemfive.android.api.SecurityAPI;
import io.onemfive.android.api.healthcare.HealthRecordAPI;
import io.onemfive.data.DocumentMessage;
import io.onemfive.data.Envelope;
import io.onemfive.data.LID;
import io.onemfive.data.HealthRecord;
import io.onemfive.data.TestReport;

/**
 * TODO: Add Definition
 * @author objectorange
 */
public class MainActivity extends AppCompatActivity {

    private BroadcastReceiver verifyLIDReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(MainActivity.class.getSimpleName(),"Received broadcast from LID verification.");
            Envelope e = (Envelope)intent.getExtras().get(Envelope.class.getName());
            LID lid = (LID)((DocumentMessage)e.getMessage()).data.get(LID.class.getName());
            if(lid.getStatus() == LID.Status.UNREGISTERED) {
                System.out.println("LID not registered.");
                createLID(lid);
            } else {
                System.out.println("LID registered.");
                authenticateLID(lid);
            }
        }
    };

    private BroadcastReceiver createLIDReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(MainActivity.class.getSimpleName(),"Received broadcast from LID creation.");
            Envelope e = (Envelope)intent.getExtras().get(Envelope.class.getName());
            LID lid = (LID)((DocumentMessage)e.getMessage()).data.get(LID.class.getName());
            if(lid.getStatus() == LID.Status.ACTIVE) {
                System.out.println("LID created.");
                loadHealthRecord(lid);
            } else {
                showError("Error creating LID.");
            }
        }
    };

    private BroadcastReceiver authenticateLIDReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(MainActivity.class.getSimpleName(),"Received broadcast from LID authN.");
            Envelope e = (Envelope)intent.getExtras().get(Envelope.class.getName());
            LID lid = (LID)((DocumentMessage)e.getMessage()).data.get(LID.class.getName());
            if(lid.getAuthenticated()) {
                System.out.println("LID authenticated.");
                loadHealthRecord(lid);
            } else {
                System.out.println("LID not authenticated.");
                showError(getResources().getText(R.string.passwordFailed).toString());
            }
        }
    };

    private BroadcastReceiver loadHealthRecordReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(MainActivity.class.getSimpleName(),"Received broadcast from Health Record loading.");
            Envelope e = (Envelope)intent.getExtras().get(Envelope.class.getName());
            HealthRecord record = (HealthRecord) ((DocumentMessage)e.getMessage()).data.get(HealthRecord.class.getName());
            if(record != null) {
                System.out.println("Health Record loaded.");
                ((DBApplication)getApplication()).setHealthRecord(record);

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
        registerReceiver(verifyLIDReceiver, new IntentFilter(SecurityAPI.LIDVerified));
        registerReceiver(createLIDReceiver, new IntentFilter(SecurityAPI.LIDCreated));
        registerReceiver(authenticateLIDReceiver, new IntentFilter(SecurityAPI.LIDAuthenticated));
        registerReceiver(loadHealthRecordReceiver, new IntentFilter(HealthRecordAPI.HealthRecordLoaded));
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(verifyLIDReceiver);
        unregisterReceiver(createLIDReceiver);
        unregisterReceiver(authenticateLIDReceiver);
        unregisterReceiver(loadHealthRecordReceiver);
        ((DBApplication)getApplication()).removeActivity(MainActivity.class);
    }

    public void verifyLID(View view) {
        Log.i(MainActivity.class.getName(),"Verifying LID...");

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
        LID lid = new LID();
        lid.setAlias(username);
        lid.setPassphrase(password);
        SecurityAPI.verifyLID(this, lid);
    }

    private void createLID(LID lid) {
        Log.i(MainActivity.class.getName(),"Creating LID...");
        SecurityAPI.createLID(this, lid);
    }

    private void authenticateLID(LID lid) {
        Log.i(MainActivity.class.getName(),"Authenticating LID...");
        SecurityAPI.authenticateLID(this, lid);
    }

    private void loadHealthRecord(LID lid) {
        Log.i(MainActivity.class.getName(),"Loading Health Record...");
        HealthRecord record = new HealthRecord();
        record.setLid(lid);
        HealthRecordAPI.loadHealthRecord(this, record);
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

    public void startTest(View view) {
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

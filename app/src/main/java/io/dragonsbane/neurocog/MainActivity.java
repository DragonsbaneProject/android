package io.dragonsbane.neurocog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import io.dragonsbane.neurocog.tests.PreTestActivity;
import io.onemfive.android.api.SecurityAPI;
import io.onemfive.android.api.healthcare.HealthRecordAPI;
import io.onemfive.android.api.util.AndroidHelper;
import io.onemfive.core.did.AuthenticateDIDRequest;
import io.onemfive.data.DID;
import io.onemfive.data.util.DLC;
import io.onemfive.data.Envelope;
import io.onemfive.data.health.HealthRecord;

/**
 * Ensure auto screen dim / lock is turned off
 *
 * @author objectorange
 */
public class MainActivity extends DBActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerReceiver(verifyDIDReceiver, new IntentFilter(SecurityAPI.DIDVerified));
        registerReceiver(createDIDReceiver, new IntentFilter(SecurityAPI.DIDCreated));
        registerReceiver(authenticateDIDReceiver, new IntentFilter(SecurityAPI.DIDAuthenticated));
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.action_bar);
        TextView titleTextView = (TextView) toolbar.getChildAt(0);
        titleTextView.setTextColor(getResources().getColor(R.color.dragonsbaneBlack));
        titleTextView.setTypeface(((DBApplication)getApplication()).getNexaBold());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(verifyDIDReceiver);
        unregisterReceiver(createDIDReceiver);
        unregisterReceiver(authenticateDIDReceiver);
        ((DBApplication)getApplication()).removeActivity(MainActivity.class);
    }

    public void toggleBACVisibility(View view) {
        boolean isBaseline = ((CheckBox)findViewById(R.id.mainCheckBoxBaseline)).isChecked();
        if(isBaseline) {
            findViewById(R.id.mainEditBAC).setVisibility(View.INVISIBLE);
        } else {
            findViewById(R.id.mainEditBAC).setVisibility(View.VISIBLE);
        }
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
        boolean baseline = ((CheckBox)findViewById(R.id.mainCheckBoxBaseline)).isChecked();
        ((DBApplication)getApplication()).setBaseline(baseline);
        if(!baseline) {
            String bacStr = ((EditText) findViewById(R.id.mainEditBAC)).getText().toString();
            ((DBApplication)getApplication()).setBac(Double.parseDouble(bacStr));
        }

        TextView messageView = findViewById(R.id.mainTextMessage);
        messageView.setVisibility(View.INVISIBLE);

        Intent i = new Intent(this, PreTestActivity.class);
        startActivity(i);
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
            Envelope e = AndroidHelper.getEnvelope(intent);
            DID did = e.getDID();
            if(!did.getVerified()) {
                Log.i(MainActivity.class.getSimpleName(),"DID not registered.");
                createDID(did);
            } else {
                Log.i(MainActivity.class.getSimpleName(),"DID registered.");
                authenticateDID(did);
            }
        }
    };

    private BroadcastReceiver createDIDReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(MainActivity.class.getSimpleName(),"Received broadcast from DID creation.");
            Envelope e = AndroidHelper.getEnvelope(intent);
            DID did = e.getDID();
            Log.i(MainActivity.class.getSimpleName(), "DID: alias="+did.getAlias()+", status="+did.getStatus().name());
            if(did.getStatus() == DID.Status.ACTIVE) {
                ((DBApplication)getApplication()).setDid(did);
                Log.i(MainActivity.class.getSimpleName(),"DID created. Starting tests...");
                startTest();
            } else {
                showError("Error creating DID.");
            }
        }
    };

    private BroadcastReceiver authenticateDIDReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(MainActivity.class.getSimpleName(),"Received broadcast from DID authN.");
            Envelope e = AndroidHelper.getEnvelope(intent);
            AuthenticateDIDRequest r = (AuthenticateDIDRequest)DLC.getData(AuthenticateDIDRequest.class, e);
            DID did = e.getDID();
            did.setAuthenticated(r.did.getAuthenticated());
            if(did.getAuthenticated()) {
                ((DBApplication)getApplication()).setDid(did);
                Log.i(MainActivity.class.getSimpleName(),"DID authenticated. Starting tests...");
                startTest();
            } else {
                Log.i(MainActivity.class.getSimpleName(),"DID not authenticated.");
                showError(getResources().getText(R.string.passwordFailed).toString());
            }
        }
    };
}

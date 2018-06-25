package io.dragonsbane.android.neurocog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import io.dragonsbane.android.DBApplication;
import io.dragonsbane.android.MainActivity;
import io.dragonsbane.android.R;
import io.onemfive.android.api.healthcare.HealthRecordAPI;
import io.onemfive.data.DID;
import io.onemfive.data.Email;
import io.onemfive.data.health.mental.memory.MemoryTest;

public class TestHistoryActivity extends AppCompatActivity {

    private List<MemoryTest> tests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_history);

        Toolbar toolbar = findViewById(R.id.action_bar);
        TextView titleTextView = (TextView) toolbar.getChildAt(0);
        titleTextView.setTextColor(getResources().getColor(R.color.dragonsbaneBlack));
        titleTextView.setTypeface(((DBApplication)getApplication()).getNexaBold());

        HealthRecordAPI.loadMemoryTestHistory(getApplicationContext(), ((DBApplication)getApplication()).getDid());
    }

//    public void sendHistory(View view) {
//        Log.i(MainActivity.class.getName(),"Sending history...");
//        DID to;
//        DID from = ((DBApplication)getApplication()).getDid();
//        String subject = "Neurocog Test Results.";
//        String message;
//        Email email = new Email(to, from, subject, message);
//        EmailAPI.sendEmail(this, email);
//    }

    public void returnHome(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}

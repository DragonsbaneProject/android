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
import io.onemfive.data.DID;
import io.onemfive.data.Email;
import io.onemfive.data.health.mental.memory.MemoryTest;

public class TestReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<MemoryTest> tests = ((DBApplication) getApplicationContext()).getTests();
        setContentView(R.layout.activity_test_report);

        Toolbar toolbar = findViewById(R.id.action_bar);
        TextView titleTextView = (TextView) toolbar.getChildAt(0);
        titleTextView.setTextColor(getResources().getColor(R.color.dragonsbaneBlack));
        titleTextView.setTypeface(((DBApplication)getApplication()).getNexaBold());
    }

//    public void sendResults(View view) {
//        Log.i(MainActivity.class.getName(),"Sending results...");
//        DID to;
//        DID from = ((DBApplication)getApplication()).getDid();
//        String subject = "Neurocog Test Results.";
//        String message;
//        Email email = new Email(to, from, subject, message);
//        EmailAPI.sendEmail(this, email);
//    }

    public void endTest(View view) {
        Intent intent = new Intent(this, TestHistoryActivity.class);
        startActivity(intent);
    }
}

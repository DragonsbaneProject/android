package io.dragonsbane.android.neurocog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import io.dragonsbane.android.DBApplication;
import io.dragonsbane.android.MainActivity;
import io.dragonsbane.android.R;

public class TestHistoryActivity extends AppCompatActivity {

    private List<ImpairmentTest> tests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_history);

        Toolbar toolbar = findViewById(R.id.action_bar);
        TextView titleTextView = (TextView) toolbar.getChildAt(0);
        titleTextView.setTextColor(getResources().getColor(R.color.dragonsbaneBlack));
        titleTextView.setTypeface(((DBApplication)getApplication()).getNexaBold());
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

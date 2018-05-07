package io.dragonsbane.android.neurocog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

    private MemoryTest.Impairment overallImpairment = MemoryTest.Impairment.None;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<MemoryTest> tests = ((DBApplication) getApplicationContext()).getTests();
        setContentView(R.layout.activity_test_report);
        for(MemoryTest t : tests) {
            determineOverallImpairment(t.getImpairment());
            switch(t.getName()) {
                case ImpairmentTestActivity.GROSS_IMPAIRMENT : {
                    ((TextView)findViewById(R.id.reportPreTestScore)).setText(t.getImpairment().name());
                    ((TextView)findViewById(R.id.reportPreTestScore)).setTextColor(getResources().getColor(ImpairmentTestActivity.getResultColor(t.getImpairment())));
                    break;
                }
                case ImpairmentTestActivity.IMPAIRMENT : {
                    ((TextView)findViewById(R.id.reportSimpleMemoryTestScore)).setText(t.getImpairment().name());
                    ((TextView)findViewById(R.id.reportSimpleMemoryTestScore)).setTextColor(getResources().getColor(ImpairmentTestActivity.getResultColor(t.getImpairment())));
                    break;
                }
                case ImpairmentTestActivity.BORDERLINE_IMPAIRMENT : {
                    ((TextView)findViewById(R.id.reportComplexMemoryTestScore)).setText(t.getImpairment().name());
                    ((TextView)findViewById(R.id.reportComplexMemoryTestScore)).setTextColor(getResources().getColor(ImpairmentTestActivity.getResultColor(t.getImpairment())));
                    break;
                }
                case ImpairmentTestActivity.NO_IMPAIRMENT : {
                    ((TextView)findViewById(R.id.reportWorkingMemoryTestScore)).setText(t.getImpairment().name());
                    ((TextView)findViewById(R.id.reportWorkingMemoryTestScore)).setTextColor(getResources().getColor(ImpairmentTestActivity.getResultColor(t.getImpairment())));
                    break;
                }
                default : {
                    System.out.println(TestReportActivity.class.getSimpleName()+": Memory Test (name="+t.getName()+") not supported in Impairment.");
                }
            }
        }
        ((TextView)findViewById(R.id.reportTotalScore)).setText(overallImpairment.name());
        ((TextView) findViewById(R.id.reportResults)).setTextColor(getResources().getColor(ImpairmentTestActivity.getResultColor(overallImpairment)));
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

    private void determineOverallImpairment(MemoryTest.Impairment testImpairment) {
        switch (testImpairment) {
            case Gross: {
                overallImpairment = MemoryTest.Impairment.Gross;
                break;
            }
            case Impaired: {
                if(overallImpairment != MemoryTest.Impairment.Gross)
                    overallImpairment = MemoryTest.Impairment.Impaired;
                break;
            }
            case Borderline: {
                if(overallImpairment == MemoryTest.Impairment.None)
                    overallImpairment = MemoryTest.Impairment.Borderline;
                break;
            }
        }
    }

    public void endTest(View view) {
        Intent intent = new Intent(this, TestHistoryActivity.class);
        startActivity(intent);
    }
}

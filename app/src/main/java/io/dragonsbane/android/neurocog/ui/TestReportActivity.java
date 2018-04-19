package io.dragonsbane.android.neurocog.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import io.dragonsbane.android.DBApplication;
import io.dragonsbane.android.R;
import io.dragonsbane.android.neurocog.persistence.Storage;
import io.dragonsbane.android.neurocog.tests.ComplexMemoryTest;
import io.dragonsbane.android.neurocog.tests.PreTestTest;
import io.dragonsbane.android.neurocog.tests.SimpleMemoryTest;
import io.dragonsbane.android.neurocog.tests.WorkingMemoryTest;
import io.onemfive.data.Test;
import io.onemfive.data.TestReport;

public class TestReportActivity extends AppCompatActivity {

    private TestReport report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DBApplication app = (DBApplication) getApplicationContext();
        report = app.getTestReport();
        report.setEnd(new Date());
        List<Test> tests = app.getTests();
        report.update(tests);
        setContentView(R.layout.activity_test_report);
        ((TextView)findViewById(R.id.reportTotalScore)).setText(report.getOverallScore());
        if(!report.getTestsFailed()) {
            ((TextView)findViewById(R.id.reportResults)).setText(getResources().getString(R.string.testPassed));
            ((TextView) findViewById(R.id.reportResults)).setTextColor(getResources().getColor(R.color.colorGrassGreen));
        } else {
            ((TextView)findViewById(R.id.reportResults)).setText(getResources().getString(R.string.testFailed));
            ((TextView) findViewById(R.id.reportResults)).setTextColor(getResources().getColor(R.color.colorWarning));
        }
        for(Test t : tests) {
            if(t instanceof PreTestTest) {
                ((TextView)findViewById(R.id.reportPreTestScore)).setText(t.getTestResult().getScore());
                if(t.getTestResult().getScore() < 80) {
                    ((TextView)findViewById(R.id.reportPreTestScore)).setTextColor(getResources().getColor(R.color.colorGrassGreen));
                } else {
                    ((TextView)findViewById(R.id.reportPreTestScore)).setTextColor(getResources().getColor(R.color.colorWarning));
                }
            } else if(t instanceof SimpleMemoryTest) {
                ((TextView)findViewById(R.id.reportSimpleMemoryTestScore)).setText(t.getTestResult().getScore());
                if(t.getTestResult().getScore() < 80) {
                    ((TextView)findViewById(R.id.reportSimpleMemoryTestScore)).setTextColor(getResources().getColor(R.color.colorGrassGreen));
                } else {
                    ((TextView)findViewById(R.id.reportSimpleMemoryTestScore)).setTextColor(getResources().getColor(R.color.colorWarning));
                }
            } else if(t instanceof ComplexMemoryTest) {
                ((TextView)findViewById(R.id.reportComplexMemoryTestScore)).setText(t.getTestResult().getScore());
                if(t.getTestResult().getScore() < 80) {
                    ((TextView)findViewById(R.id.reportComplexMemoryTestScore)).setTextColor(getResources().getColor(R.color.colorGrassGreen));
                } else {
                    ((TextView)findViewById(R.id.reportComplexMemoryTestScore)).setTextColor(getResources().getColor(R.color.colorWarning));
                }
            } else if(t instanceof WorkingMemoryTest) {
                ((TextView)findViewById(R.id.reportWorkingMemoryTestScore)).setText(t.getTestResult().getScore());
                if(t.getTestResult().getScore() < 80) {
                    ((TextView)findViewById(R.id.reportWorkingMemoryTestScore)).setTextColor(getResources().getColor(R.color.colorGrassGreen));
                } else {
                    ((TextView)findViewById(R.id.reportWorkingMemoryTestScore)).setTextColor(getResources().getColor(R.color.colorWarning));
                }
            }
        }
    }

    public void endTest(View view) {
        // Ensure App is cleared
        DBApplication app = (DBApplication) getApplicationContext();
        if(report.getBaseline()) {
            try {
                Storage.writeInternalObject(this, app.getHealthRecord().getLid().getAlias()+".baseline", report);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        app.clearTests();
        Intent intent = new Intent(this, TestHistoryActivity.class);
        startActivity(intent);
    }
}

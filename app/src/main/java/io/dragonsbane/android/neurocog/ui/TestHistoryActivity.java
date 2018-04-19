package io.dragonsbane.android.neurocog.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import io.dragonsbane.android.R;
import io.onemfive.data.TestReport;

public class TestHistoryActivity extends AppCompatActivity {

    private List<TestReport> reports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_history);
    }
}

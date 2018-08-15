package io.dragonsbane.android.neurocog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import io.dragonsbane.android.DBApplication;
import io.dragonsbane.android.R;
import io.dragonsbane.android.service.ServiceAPI;
import io.onemfive.android.api.util.AndroidHelper;
import io.onemfive.data.Envelope;
import io.onemfive.data.health.mental.memory.MemoryTest;
import io.onemfive.data.util.DLC;

public class ExportHistoryActivity extends AppCompatActivity {

    private List<MemoryTest> tests;
    private byte[] testHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((DBApplication)getApplication()).addActivity(ExportHistoryActivity.class, this);
        registerReceiver(loadTestsReceiver, new IntentFilter(ServiceAPI.MEMORY_TESTS_LOADED));
        ServiceAPI.loadTests(this);
        setContentView(R.layout.activity_export_history);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(loadTestsReceiver);
        ((DBApplication)getApplication()).removeActivity(ExportHistoryActivity.class);
    }

    private BroadcastReceiver loadTestsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Envelope e = AndroidHelper.getEnvelope(intent);
            tests = (List<MemoryTest>)DLC.getEntity(e);
        }
    };

    private void buildTestHistory() {
        if(tests != null) {
            // Ensure Tests are sorted descending by time ended
            Collections.sort(tests, new Comparator<MemoryTest>() {
                @Override
                public int compare(MemoryTest t1, MemoryTest t2) {
                    if(t1.getTimeEnded().getTime() > t2.getTimeEnded().getTime()) return 1;
                    else return -1;
                }
            });
            StringBuilder s = new StringBuilder();
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ",Locale.US);
            // Header
            s.append("id,did,name,baseline,timeStarted,timeEnded,difficulty,successes,successResponseTimes,maxResponseTimeSuccess,avgResponseTimeSuccess,minResponseTimeSuccess,misses,missResponseTimes,maxResponsetimeMiss,avgResponseTimeMiss,minResponseTimeMiss,negatives,negativeResponseTimes,maxResponseTimeNegative,avgResponseTimeNegative,minResponseTimeNegative,inappropriate,inappropriateResponseTimes,maxResponseTimeInappropriate,avgResponseTimeInappropriate,minResponseTimeInappropriate,cardsUsed,bac,score\r");
            for(MemoryTest t : tests) {
                s.append(t.getId());s.append(",");
                s.append(t.getDid());s.append(",");
                s.append(t.getName());s.append(",");
                s.append(t.getBaseline().toString());s.append(",");
                s.append(f.format(t.getTimeStarted()));s.append(",");
                s.append(f.format(t.getTimeEnded()));s.append(",");

            }
        }
    }
}

package io.dragonsbane.android.neurocog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.ArrayList;
import java.util.List;

import io.dragonsbane.android.DBApplication;
import io.dragonsbane.android.R;
import io.onemfive.android.api.healthcare.HealthRecordAPI;
import io.onemfive.data.DID;
import io.onemfive.data.health.mental.memory.MemoryTest;

/**
 * TODO: Add Definition
 *
 * @author ObjectOrange
 */

public abstract class ImpairmentTestActivity extends AppCompatActivity implements Animation.AnimationListener {

    public static final String GROSS_IMPAIRMENT = "GrossImpairment";
    public static final String IMPAIRMENT = "Impairment";
    public static final String BORDERLINE_IMPAIRMENT = "BorderlineImpairment";
    public static final String NO_IMPAIRMENT = "NoImpairment";

    protected MemoryTest memoryTest;
    protected Animation animation1;
    protected Animation animation2;

    protected Long begin;
    protected Long end;
    protected List<Long> inappropriateResponseTimes = new ArrayList<>();
    protected List<Long> negativeResponseTimes = new ArrayList<>();
    protected List<Long> successResponseTimes = new ArrayList<>();

    protected DBApplication app;
    protected DID did;
    protected Double bac = 0.0D;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (DBApplication)getApplication();
        did = app.getDid();
        bac = app.getBac();
        animation1 = AnimationUtils.loadAnimation(this, R.anim.to_middle);
        animation1.setAnimationListener(this);
        animation2 = AnimationUtils.loadAnimation(this, R.anim.from_middle);
        animation2.setAnimationListener(this);
    }

    public static int getResultColor(MemoryTest.Impairment impairment) {
        switch(impairment) {
            case Gross: return R.color.colorWarning;
            case Impaired: return R.color.colorOrange;
            case Borderline: return R.color.colorYellow;
            case Unimpaired: return R.color.colorGrassGreen;
            default: return R.color.colorGrassGreen;
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    protected void testFinished() {
        // Determine inappropriate measurements
        Long minInappropriateResponseTime = 0L;
        Long maxInappropriateResponseTime = 0L;
        Long totalInappropriateResponseTimes = 0L;
        for(Long t : inappropriateResponseTimes) {
            if (minInappropriateResponseTime == 0L) {
                minInappropriateResponseTime = t;
                maxInappropriateResponseTime = t;
            } else if(t < minInappropriateResponseTime) {
                minInappropriateResponseTime = t;
            } else if(t > maxInappropriateResponseTime) {
                maxInappropriateResponseTime = t;
            }
            totalInappropriateResponseTimes += t;
        }
        memoryTest.setMinResponseTimeInappropriateMs(minInappropriateResponseTime);
        memoryTest.setMaxResponseTimeInappropriateMs(maxInappropriateResponseTime);
        memoryTest.setAvgResponseTimeInappropriateMs(totalInappropriateResponseTimes / inappropriateResponseTimes.size());

        // Determine negative measurements
        Long minNegativeResponseTime = 0L;
        Long maxNegativeResponseTime = 0L;
        Long totalNegativeResponseTimes = 0L;
        for(Long t : negativeResponseTimes) {
            if (minNegativeResponseTime == 0L) {
                minNegativeResponseTime = t;
                maxNegativeResponseTime = t;
            } else if(t < minNegativeResponseTime) {
                minNegativeResponseTime = t;
            } else if(t > maxNegativeResponseTime) {
                maxNegativeResponseTime = t;
            }
            totalNegativeResponseTimes += t;
        }
        memoryTest.setMinResponseTimeNegativeMs(minNegativeResponseTime);
        memoryTest.setMaxResponseTimeNegativeMs(maxNegativeResponseTime);
        memoryTest.setAvgResponseTimeNegativeMs(totalNegativeResponseTimes / negativeResponseTimes.size());

        // Determine success measurements
        Long minSuccessResponseTime = 0L;
        Long maxSuccessResponseTime = 0L;
        Long totalSuccessResponseTimes = 0L;
        for(Long t : successResponseTimes) {
            if (minSuccessResponseTime == 0L) {
                minSuccessResponseTime = t;
                maxSuccessResponseTime = t;
            } else if(t < minSuccessResponseTime) {
                minSuccessResponseTime = t;
            } else if(t > maxSuccessResponseTime) {
                maxSuccessResponseTime = t;
            }
            totalSuccessResponseTimes += t;
        }
        memoryTest.setMinResponseTimeSuccessMs(minSuccessResponseTime);
        memoryTest.setMaxResponseTimeSuccessMs(maxSuccessResponseTime);
        memoryTest.setAvgResponseTimeSuccessMs(totalSuccessResponseTimes / successResponseTimes.size());

        HealthRecordAPI.saveMemoryTest(getApplicationContext(), did, memoryTest);
        app.addTest(memoryTest);
    }

}

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

    protected DBApplication app;
    protected DID did;
    protected Double bac = 0.0D;
    protected Boolean baseline = false;
    protected long normalFlipDurationMs = 3 * 1000;
    protected long shortFlipDuractionMs = 1000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (DBApplication)getApplication();
        did = app.getDid();
        if(app.getBac() != null)
            bac = app.getBac();
        if(app.getBaseline() != null)
            baseline = app.getBaseline();
        animation1 = AnimationUtils.loadAnimation(this, R.anim.to_middle);
        animation1.setAnimationListener(this);
        animation2 = AnimationUtils.loadAnimation(this, R.anim.from_middle);
        animation2.setAnimationListener(this);
    }

//    public static int getResultColor(MemoryTest.Impairment impairment) {
//        switch(impairment) {
//            case Gross: return R.color.colorWarning;
//            case Impaired: return R.color.colorOrange;
//            case Borderline: return R.color.colorYellow;
//            case Unimpaired: return R.color.colorGrassGreen;
//            default: return R.color.colorGrassGreen;
//        }
//    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    protected void testFinished() {
        HealthRecordAPI.saveMemoryTest(getApplicationContext(), did, memoryTest);
        app.addTest(memoryTest);
    }

}

package io.dragonsbane.neurocog.tests;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import io.dragonsbane.data.ImpairmentTest;
import io.dragonsbane.neurocog.DBActivity;
import io.dragonsbane.neurocog.DBApplication;
import io.dragonsbane.neurocog.R;
import io.dragonsbane.neurocog.ServiceAPI;
import io.onemfive.data.DID;

/**
 * TODO: Add Definition
 *
 * @author ObjectOrange
 */

public abstract class ImpairmentTestActivity extends DBActivity implements Animation.AnimationListener {

    public static final String GROSS_IMPAIRMENT = "GrossImpairment";
    public static final String IMPAIRMENT = "Impairment";
    public static final String BORDERLINE_IMPAIRMENT = "BorderlineImpairment";
    public static final String NO_IMPAIRMENT = "NoImpairment";

    protected ImpairmentTest test;
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
        test.setDid(app.getDid());
        ServiceAPI.saveTest(this, test);
        app.addTest(test);
    }

}

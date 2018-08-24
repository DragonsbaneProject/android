package io.dragonsbane.android;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.dragonsbane.android.database.Storage;
import io.dragonsbane.android.tests.ImpairmentTest;
import io.dragonsbane.android.service.DragonsbaneAndroidService;
import io.onemfive.android.api.util.AndroidHelper;
import io.onemfive.core.util.Numbers;
import io.onemfive.data.DID;
import io.onemfive.data.health.HealthRecord;

/**
 * Dragonsbane
 * @author objectorange
 */
public class DBApplication extends Application {

    private DID did;
    private HealthRecord healthRecord;
    private Double bac = 0.0D;
    private Boolean baseline = false;
    private List<ImpairmentTest> tests = new ArrayList<>();
    private Map<String,Activity> activities = new HashMap<>();
    private Typeface nexa_bold;
    private Typeface nexa_light;

    private Storage storage;

    public static int[] cards = {
            R.drawable.card_c2, R.drawable.card_c3, R.drawable.card_c4, R.drawable.card_c5,
            R.drawable.card_c6, R.drawable.card_c7, R.drawable.card_c8, R.drawable.card_c9,
            R.drawable.card_c10, R.drawable.card_cj, R.drawable.card_cq, R.drawable.card_ck,
            R.drawable.card_ca,
            R.drawable.card_d2, R.drawable.card_d3, R.drawable.card_d4, R.drawable.card_d5,
            R.drawable.card_d6, R.drawable.card_d7, R.drawable.card_d8, R.drawable.card_d9,
            R.drawable.card_d10, R.drawable.card_dj, R.drawable.card_dq, R.drawable.card_dk,
            R.drawable.card_da,
            R.drawable.card_h2, R.drawable.card_h3, R.drawable.card_h4, R.drawable.card_h5,
            R.drawable.card_h6, R.drawable.card_h7, R.drawable.card_h8, R.drawable.card_h9,
            R.drawable.card_h10, R.drawable.card_hj, R.drawable.card_hq, R.drawable.card_hk,
            R.drawable.card_ha,
            R.drawable.card_s2, R.drawable.card_s3, R.drawable.card_s4, R.drawable.card_s5,
            R.drawable.card_s6, R.drawable.card_s7, R.drawable.card_s8, R.drawable.card_s9,
            R.drawable.card_s10, R.drawable.card_sj, R.drawable.card_sq, R.drawable.card_sk,
            R.drawable.card_sa
    };

    public DID getDid() {
        return did;
    }

    public void setDid(DID did) {
        this.did = did;
    }

    public HealthRecord getHealthRecord() {
        return healthRecord;
    }

    public void setHealthRecord(HealthRecord healthRecord) {
        this.healthRecord = healthRecord;
    }

    public Double getBac() {
        return bac;
    }

    public void setBac(Double bac) {
        this.bac = bac;
    }

    public Boolean getBaseline() {
        return baseline;
    }

    public void setBaseline(Boolean baseline) {
        this.baseline = baseline;
    }

    public Typeface getNexaBold() {
        return nexa_bold;
    }

    public Typeface getNexaLight() {
        return nexa_light;
    }

    public void addTest(ImpairmentTest test) {
        tests.add(test);
    }

    public List<ImpairmentTest> getTests() {
        return tests;
    }

    public void clearTests() {
        tests.clear();;
    }

    public Activity getActivity(Class clazz) {
        return activities.get(clazz.getName());
    }

    public void addActivity(Class clazz, DBActivity activity) {
        activity.setStorage(storage);
        activities.put(clazz.getName(), activity);
    }

    public void removeActivity(Class clazz) {
        activities.remove(clazz.getName());
    }

    public int getRandomCard() {
        return cards[Numbers.randomNumber(0, cards.length-1)];
    }

    public int getRandomCard(int min, int max) {
        return cards[Numbers.randomNumber(min, max)];
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // This is REQUIRED to communicate with 1M5 via Android
        AndroidHelper.serviceClass = DragonsbaneAndroidService.class;

        // Fonts
        nexa_bold = Typeface.createFromAsset(getAssets(),"fonts/nexa_bold.otf");
        nexa_light = Typeface.createFromAsset(getAssets(),"fonts/nexa_light.otf");

        // Ensure user DID available
        did = new DID();

        storage = new Storage(this, 2);

        // Start Router Service
        Intent i = new Intent(this, DragonsbaneAndroidService.class);
        Log.i(DBApplication.class.getName(),"Starting DragonsbaneAndroidService to bootstrap 1M5 Core...");
        startService(i);

        // Register Dragonsbane services with 1M5
//        List<Class> servicesToRegister = new ArrayList<>();
//        servicesToRegister.add(MLService.class);
//        Properties p = new Properties();
//        AdminAPI.registerServices(this, servicesToRegister, p);
    }
}

package io.dragonsbane.android;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.dragonsbane.android.util.Numbers;
import io.onemfive.android.api.service.OneMFiveAndroidRouterService;
import io.onemfive.data.DID;
import io.onemfive.data.Test;
import io.onemfive.data.TestReport;

/**
 * TODO: Add Definition
 * @author objectorange
 */
public class DBApplication extends Application {

    private DID did;
    private Map<String,Object> healthRecord;
    private TestReport report;
    private List<Test> tests = new ArrayList<>();
    private Map<String,Activity> activities = new HashMap<>();
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

    public Map<String,Object> getHealthRecord() {
        return healthRecord;
    }

    public void setHealthRecord(Map<String,Object> healthRecord) {
        this.healthRecord = healthRecord;
    }

    public void setTestReport(TestReport report) {
        this.report = report;
    }

    public TestReport getTestReport() {
        return report;
    }

    public void addTest(Test test) {
        tests.add(test);
    }

    public List<Test> getTests() {
        return tests;
    }

    public void clearTests() {
        tests.clear();;
    }

    public Activity getActivity(Class clazz) {
        return activities.get(clazz.getName());
    }

    public void addActivity(Class clazz, Activity activity) {
        activities.put(clazz.getName(), activity);
    }

    public void removeActivity(Class clazz) {
        activities.remove(clazz.getName());
    }

    public int getRandomCard() {
        return cards[Numbers.randomNumber(1, 52)];
    }

    public int getRandomCard(int min, int max) {
        return cards[Numbers.randomNumber(min, max)];
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Intent i = new Intent(this, OneMFiveAndroidRouterService.class);
        Log.i(DBApplication.class.getName(),"Starting OneMFiveAndroidRouterService to bootstrap 1M5 Core...");
        startService(i);
    }
}

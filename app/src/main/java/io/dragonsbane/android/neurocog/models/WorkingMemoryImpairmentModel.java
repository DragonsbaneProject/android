package io.dragonsbane.android.neurocog.models;

import io.dragonsbane.android.neurocog.tests.WorkingMemoryTest;
import io.onemfive.data.health.Test;
import io.onemfive.data.health.TestResult;

/**
 * Created by Brian on 3/10/18.
 */

public class WorkingMemoryImpairmentModel extends BACImpairmentModel {

    @Override
    public void evaluate(Test test) {
        WorkingMemoryTest t = (WorkingMemoryTest)test;

        TestResult r = test.getTestResult();

        // Misses - Should have clicked but did not
        if(t.misses == 1) {
            r.addToScore(10);
        } else if(t.misses == 2) {
            r.addToScore(20);
        } else if(t.misses == 3) {
            r.addToScore(40);
        } else if(t.misses == 4) {
            r.addToScore(60);
        } else if(t.misses > 4) {
            r.addToScore(80);
        }

        // Negatives - Face showing but should not have clicked
        if(t.negative == 1) {
            r.addToScore(10);
        } else if(t.negative == 2) {
            r.addToScore(20);
        } else if(t.negative == 3) {
            r.addToScore(40);
        } else if(t.negative == 4) {
            r.addToScore(60);
        } else if(t.negative > 4) {
            r.addToScore(80);
        }


        // Inappropriate - Back showing when clicked
        if(t.inappropriate == 1) {
            r.addToScore(40);
        } else if(t.inappropriate > 1) {
            r.addToScore(80);
        }

    }
}

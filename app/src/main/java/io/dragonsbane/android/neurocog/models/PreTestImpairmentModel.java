package io.dragonsbane.android.neurocog.models;

import io.dragonsbane.android.neurocog.tests.PreTestTest;
import io.onemfive.data.Test;
import io.onemfive.data.TestResult;

/**
 * Impairment Model for Pre-Test
 *
 * Determines impairment by determining % likelihood of impairment
 * based on average population response time to 0.080 BAC level.
 */

public class PreTestImpairmentModel extends BACImpairmentModel {

    public int maxAllowedDelay = 1000; // 1000ms (1 seconds)

    @Override
    public void evaluate(Test test) {
        PreTestTest t = (PreTestTest)test;

        TestResult r = test.getTestResult();

        // Inappropriate Clicks
        if(t.inappropriate == 1) {
            r.addToScore(20);
        } else if(t.inappropriate == 2) {
            r.addToScore(40);
        } else if(t.inappropriate > 2) {
            r.addToScore(80);
        }

        // Max Delay
        int numberOfMaxAllowedDelay = 0;
        if(t.msBetween1stFlippedAndClicked > maxAllowedDelay) numberOfMaxAllowedDelay++;
        if(t.msBetween2ndFlippedAndClicked > maxAllowedDelay) numberOfMaxAllowedDelay++;
        if(t.msBetween3rdFlippedAndClicked > maxAllowedDelay) numberOfMaxAllowedDelay++;
        if(t.msBetween4thFlippedAndClicked > maxAllowedDelay) numberOfMaxAllowedDelay++;
        if(t.msBetween5thFlippedAndClicked > maxAllowedDelay) numberOfMaxAllowedDelay++;
        if(numberOfMaxAllowedDelay == 1) {
            r.addToScore(40);
        } else if(numberOfMaxAllowedDelay > 1) {
            r.addToScore(80);
        }

        // Delay Span
        if(t.msBetween1stFlippedAndClicked - maxAllowedDelay > 1000) r.addToScore(40);
        if(t.msBetween1stFlippedAndClicked - maxAllowedDelay > 1000) r.addToScore(40);
        if(t.msBetween1stFlippedAndClicked - maxAllowedDelay > 1000) r.addToScore(40);
        if(t.msBetween1stFlippedAndClicked - maxAllowedDelay > 1000) r.addToScore(40);
        if(t.msBetween1stFlippedAndClicked - maxAllowedDelay > 1000) r.addToScore(40);

        if(t.msBetween1stFlippedAndClicked - maxAllowedDelay > 500) r.addToScore(20);
        if(t.msBetween1stFlippedAndClicked - maxAllowedDelay > 500) r.addToScore(20);
        if(t.msBetween1stFlippedAndClicked - maxAllowedDelay > 500) r.addToScore(20);
        if(t.msBetween1stFlippedAndClicked - maxAllowedDelay > 500) r.addToScore(20);
        if(t.msBetween1stFlippedAndClicked - maxAllowedDelay > 500) r.addToScore(20);

    }
}

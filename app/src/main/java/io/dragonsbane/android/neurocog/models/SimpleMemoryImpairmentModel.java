package io.dragonsbane.android.neurocog.models;

import io.dragonsbane.android.neurocog.Test;
import io.dragonsbane.android.neurocog.TestResult;
import io.dragonsbane.android.neurocog.tests.SimpleMemoryTest;

/**
 * Created by Brian on 3/10/18.
 */

public class SimpleMemoryImpairmentModel extends ImpairmentModelBase {

    @Override
    public void evaluate(Test test) {
        SimpleMemoryTest t = (SimpleMemoryTest)test;

        TestResult r = test.getTestResult();

        // Misses - Should have clicked but did not
        if(t.misses == 1) {
            r.addToScore(10);
        } else if(t.misses == 2) {
            r.addToScore(20);
        } else if(t.misses > 2) {
            r.addToScore(80);
        }

        // Negatives - Face showing but should not have clicked
        if(t.negative == 1) {
            r.addToScore(10);
        } else if(t.negative == 2) {
            r.addToScore(20);
        } else if(t.negative > 2) {
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

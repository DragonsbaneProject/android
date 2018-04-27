package io.dragonsbane.android.neurocog.tests;

import java.util.List;

import io.dragonsbane.android.neurocog.Test;
import io.dragonsbane.android.neurocog.TestResult;


/**
 * The camera is watching for eye blinks the entire time. There may also be
 * something we can do with pupil direction if I understand that right.
 */
public class EyeBlinkTest implements Test {

    @Override
    public TestResult getTestResult() {
        return null;
    }

    @Override
    public List<Integer> cardsUsed() {
        return null;
    }
}

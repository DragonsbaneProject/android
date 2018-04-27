package io.dragonsbane.android.neurocog.tests;

import java.util.ArrayList;
import java.util.List;

import io.dragonsbane.android.neurocog.Test;
import io.dragonsbane.android.neurocog.TestResult;


public class SimpleMemoryTest implements Test {

    private TestResult testResult = new TestResult();
    public List<Integer> cardsUsed = new ArrayList<>();

    public int successes = 0; // Clicked when should have
    public int misses = 0; // Should have clicked but did not
    public int negative = 0; // Face showing but should not have clicked
    public int inappropriate = 0; // Back showing when clicked

    @Override
    public List<Integer> cardsUsed() {
        return cardsUsed;
    }

    @Override
    public TestResult getTestResult() {
        return testResult;
    }
}

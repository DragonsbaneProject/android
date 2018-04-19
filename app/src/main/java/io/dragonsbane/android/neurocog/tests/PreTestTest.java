package io.dragonsbane.android.neurocog.tests;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.dragonsbane.android.R;
import io.onemfive.data.Test;
import io.onemfive.data.TestResult;

public class PreTestTest implements Test {

    private TestResult testResult = new TestResult();
    public List<Integer> cardsUsed = new ArrayList<>();

    private int cardFlipped = 0;

    public int inappropriate = 0;

    private long begin1stFlippedMS = 0;
    private long end1stClickedMS = 0;
    public long msBetween1stFlippedAndClicked = 0;

    private long begin2ndFlippedMS = 0;
    private long end2ndClickedMS = 0;
    public long msBetween2ndFlippedAndClicked = 0;

    private long begin3rdFlippedMS = 0;
    private long end3rdClickedMS = 0;
    public long msBetween3rdFlippedAndClicked = 0;

    private long begin4thFlippedMS = 0;
    private long end4thClickedMS = 0;
    public long msBetween4thFlippedAndClicked = 0;

    private long begin5thFlippedMS = 0;
    private long end5thClickedMS = 0;
    public long msBetween5thFlippedAndClicked = 0;

    public PreTestTest() {
        cardsUsed.add(R.drawable.card_ca);
    }

    @Override
    public List<Integer> cardsUsed() {
        return cardsUsed;
    }

    @Override
    public TestResult getTestResult() {
        return testResult;
    }

    public void flippedCard() {
        long now = new Date().getTime();
        switch(++cardFlipped) {
            case 1: begin1stFlippedMS = now;break;
            case 2: begin2ndFlippedMS = now;break;
            case 3: begin3rdFlippedMS = now;break;
            case 4: begin4thFlippedMS = now;break;
            case 5: begin5thFlippedMS = now;break;
        }
    }

    public void clickedCard(){
        long now = new Date().getTime();

        // Card clicked is same as last card flipped => success
        switch(cardFlipped) {
            case 1: {
                end1stClickedMS = now;
                msBetween1stFlippedAndClicked = end1stClickedMS - begin1stFlippedMS;
                break;
            }
            case 2: {
                end2ndClickedMS = now;
                msBetween2ndFlippedAndClicked = end2ndClickedMS - begin2ndFlippedMS;
                break;
            }
            case 3: {
                end3rdClickedMS = now;
                msBetween3rdFlippedAndClicked = end3rdClickedMS - begin3rdFlippedMS;
                break;
            }
            case 4: {
                end4thClickedMS = now;
                msBetween4thFlippedAndClicked = end4thClickedMS - begin4thFlippedMS;
                break;
            }
            case 5: {
                end5thClickedMS = now;
                msBetween5thFlippedAndClicked = end5thClickedMS - begin5thFlippedMS;
            }
        }
    }

}

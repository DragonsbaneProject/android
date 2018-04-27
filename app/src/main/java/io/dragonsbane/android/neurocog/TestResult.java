package io.dragonsbane.android.neurocog;

import java.io.Serializable;

/**
 * TODO: Add Definition
 *
 * @author Brian on 4/26/18
 */

public class TestResult implements Serializable {

    private int score = 0;

    public void addToScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

}

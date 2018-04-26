package io.dragonsbane.android.neurocog;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * TODO: Add Definition
 *
 * @author Brian on 4/26/18
 */

public class TestReport implements Serializable {

    private boolean baseline = false;
    private Date start;
    private Date end;
    private List<Test> tests;

    public void update(List<Test> tests) {
        this.tests = tests;
    }

    public boolean getBaseline() {
        return baseline;
    }

    public void setBaseline(boolean baseline) {
        this.baseline = baseline;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public int getOverallScore() {
        int overallScore = 0;
        for(Test t : tests) {
            overallScore += t.getTestResult().getScore();
        }
        return overallScore;
    }

    public boolean getTestsFailed() {
        boolean testsFailed = false;
        for(Test t : tests) {
            if(t.getTestResult().getScore() >= 80) testsFailed = true;
        }
        return testsFailed;
    }

}

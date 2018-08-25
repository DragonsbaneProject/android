package io.dragonsbane.neurocog.tests;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import io.onemfive.data.DID;
import io.onemfive.data.JSONSerializable;

public class ImpairmentTest implements JSONSerializable {

    private DID did;
    private String name;
    private Boolean baseline = false;
    private Long timeStarted;
    private Long timeEnded;
    private Double bloodAlcoholContent;

    private List<Long> successes = new ArrayList<>();
    private List<Long> misses = new ArrayList<>();
    private List<Long> inappropriates = new ArrayList<>();
    private List<Long> negatives = new ArrayList<>();

    List<Integer> cardsUsed = new ArrayList<>();

    public ImpairmentTest(DID did, String name) {
        this.did = did;
        this.name = name;
    }

    public String getId() {
        return did.getAlias()+":"+timeStarted;
    }

    public DID getDid() {
        return did;
    }

    public void setDid(DID did) {
        this.did = did;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getBaseline() {
        return baseline;
    }

    public void setBaseline(Boolean baseline) {
        this.baseline = baseline;
    }

    public Long getTimeStarted() {
        return timeStarted;
    }

    public void setTimeStarted(Long timeStarted) {
        this.timeStarted = timeStarted;
    }

    public Long getTimeEnded() {
        return timeEnded;
    }

    public void setTimeEnded(Long timeEnded) {
        this.timeEnded = timeEnded;
    }

    public Double getBloodAlcoholContent() {
        return bloodAlcoholContent;
    }

    public void setBloodAlcoholContent(Double bloodAlcoholContent) {
        this.bloodAlcoholContent = bloodAlcoholContent;
    }

    public void addSuccess(long responseTime) {
        successes.add(responseTime);
    }

    public int getSuccesses() {
        return successes.size();
    }

    public long getMinResponseTimeSuccessMs() {
        long min = 24 * 60 * 60 * 1000L;
        for(long s : successes) {
            if(s < min) min = s;
        }
        if(min ==24 * 60 * 60 * 1000L)
            return 0;
        return min;
    }

    public long getMaxResponseTimeSuccessMs() {
        long max = 0L;
        for(long s : successes) {
            if(s > max) max = s;
        }
        return max;
    }

    public long getAvgResponseTimeSuccessMs() {
        long sum = 0L;
        for(long s : successes){
            sum += s;
        }
        if(sum > 0)
            return sum / successes.size();
        else
            return 0;
    }

    public void addMiss(long responseTime) {
        misses.add(responseTime);
    }

    public int getMisses() {
        return misses.size();
    }

    public long getMinResponseTimeMissMs() {
        long min = 24 * 60 * 60 * 1000L;
        for(long s : misses) {
            if(s < min) min = s;
        }
        if(min ==24 * 60 * 60 * 1000L)
            return 0;
        return min;
    }

    public long getMaxResponseTimeMissMs() {
        long max = 0L;
        for(long s : misses) {
            if(s > max) max = s;
        }
        return max;
    }

    public long getAvgResponseTimeMissMs() {
        long sum = 0L;
        for(long s : misses){
            sum += s;
        }
        if(sum > 0)
            return sum / misses.size();
        else
            return 0;
    }

    public void addInappropriate(long responseTime) {
        inappropriates.add(responseTime);
    }

    public int getInappropriates() {
        return inappropriates.size();
    }

    public long getMinResponseTimeInappropriateMs() {
        long min = 24 * 60 * 60 * 1000L;
        for(long s : inappropriates) {
            if(s < min) min = s;
        }
        if(min ==24 * 60 * 60 * 1000L)
            return 0;
        return min;
    }

    public long getMaxResponseTimeInappropriateMs() {
        long max = 0L;
        for(long s : inappropriates) {
            if(s > max) max = s;
        }
        return max;
    }

    public long getAvgResponseTimeInappropriateMs() {
        long sum = 0L;
        for(long s : inappropriates){
            sum += s;
        }
        if(sum > 0)
            return sum / inappropriates.size();
        else
            return 0;
    }

    public void addNegative(long responseTime) {
        negatives.add(responseTime);
    }

    public int getNegatives() {
        return negatives.size();
    }

    public long getMinResponseTimeNegativeMs() {
        long min = 24 * 60 * 60 * 1000L;
        for(long s : negatives) {
            if(s < min) min = s;
        }
        if(min ==24 * 60 * 60 * 1000L)
            return 0;
        return min;
    }

    public long getMaxResponseTimeNegativeMs() {
        long max = 0L;
        for(long s : negatives) {
            if(s > max) max = s;
        }
        return max;
    }

    public long getAvgResponseTimeNegativeMs() {
        long sum = 0L;
        for(long s : negatives){
            sum += s;
        }
        if(sum > 0)
            return sum / negatives.size();
        else
            return 0;
    }

    public List<Integer> getCardsUsed() {
        return cardsUsed;
    }

    @Override
    public Map<String, Object> toMap() {
        return null;
    }

    @Override
    public void fromMap(Map<String, Object> map) {

    }
}

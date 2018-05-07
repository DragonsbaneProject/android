package io.dragonsbane.android.neurocog;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import io.dragonsbane.android.DBApplication;
import io.dragonsbane.android.R;
import io.onemfive.core.util.Numbers;
import io.onemfive.data.DID;
import io.onemfive.data.health.mental.memory.MemoryTest;

/**
 * Have you seen this tile during this session. There’s not so much attention here as working
 * memory and also tests for reality testing. Of course the person has seen each tile at
 * some time in their lives so that dream like time quality that comes with sleep deprivation
 * will be tests. We’d show tiles over a period of a minute that were shown and not shown and
 * measure the right and wrong taps from the person.
 */
public class WorkingMemoryTestActivity extends ImpairmentTestActivity {

    public List<Integer> cardsUsedPrior = new ArrayList<>();
    public List<Integer> cardsNotUsedPrior = new ArrayList<>();

    private int numberFlips = 12;
    private int currentCard = 0;
    private int currentNumberFlips = 0;
    private Long begin;
    private Long end;
    private List<Long> responseTimes = new ArrayList<>();

    private boolean isBackOfCardShowing = true;
    private boolean shouldNotClick = false;

    private FlipCard flipCard;
    private EndTest endTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DID did = ((DBApplication)getApplication()).getDid();
        memoryTest = MemoryTest.newInstance(NO_IMPAIRMENT,did.getId());
        DBApplication app = (DBApplication) getApplicationContext();
        app.addActivity(WorkingMemoryTestActivity.class, this);
        setContentView(R.layout.activity_working_memory_test);
        List<MemoryTest> tests = ((DBApplication)getApplication()).getTests();
        for(MemoryTest test : tests) {
            cardsUsedPrior.addAll(test.cardsUsed());
        }
        int numberCardsUsedPrior = cardsUsedPrior.size();
        for(int i = 0; i<numberCardsUsedPrior;i++) {
            int card = 0;
            boolean skip = true;
            int runaway = 100;
            while(skip && runaway-- > 0) {
                card = app.getRandomCard();
                // don't use a card already used in past tests nor already added
                skip = cardsUsedPrior.contains(card) ||
                        cardsNotUsedPrior.contains(card);
            }
            if(runaway > 0) // make sure break out not due to runaway
                cardsNotUsedPrior.add(card);
        }

        flipCard = new FlipCard();
        new Handler().postDelayed(flipCard, 3 * 1000); // flip card after 3 seconds
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((DBApplication)getApplication()).removeActivity(WorkingMemoryTestActivity.class);
    }

    public void clickCard(View v) {
        if(isBackOfCardShowing) {memoryTest.addInappropriate();return;}
        if(shouldNotClick) {memoryTest.addMiss();return;}
        memoryTest.addSuccess();
        end = new Date().getTime();
        long diff = end - begin;
        responseTimes.add(diff);
        Long totalResponseTime = 0L;
        for(Long responseTime : responseTimes) {
            totalResponseTime += responseTime;
        }
        memoryTest.setAvgResponseTimeMs(totalResponseTime/currentNumberFlips);
        if(diff < memoryTest.getMinReponseTimeMs())
            memoryTest.setMinReponseTimeMs(diff);
        else if(diff > memoryTest.getMaxResponseTimeMs())
            memoryTest.setMaxResponseTimeMs(diff);
        memoryTest.addSuccess();
        flipCard.deactivate(); // Deactivate prior FlipCard
        v.setEnabled(false);
        v.clearAnimation();
        v.setAnimation(animation1);
        v.startAnimation(animation1);
    }

    @Override
    public void onAnimationStart(Animation animation) {
        if(animation == animation1 && !isBackOfCardShowing && !shouldNotClick) {
            // Should have clicked and did not
            memoryTest.addMiss();
        }
        if (animation == animation2) {
            if(isBackOfCardShowing) {
                begin = new Date().getTime();
            }
        }
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        shouldNotClick = new Random().nextBoolean();
        if (animation == animation1) {
            // End of 1st half of flip
            if (isBackOfCardShowing) {
                numberFlips--;
                if(shouldNotClick) {
                    int random = Numbers.randomNumber(0,cardsNotUsedPrior.size()-1);
                    currentCard = cardsNotUsedPrior.get(random);
                } else {
                    int random = Numbers.randomNumber(0,cardsUsedPrior.size()-1);
                    currentCard = cardsUsedPrior.get(random);
                }
                if(!memoryTest.cardsUsed().contains(currentCard)) memoryTest.cardsUsed().add(currentCard);
                (findViewById(R.id.workingMemoryTestCard)).setBackground(getResources().getDrawable(currentCard));
            } else {
                (findViewById(R.id.workingMemoryTestCard)).setBackground(getResources().getDrawable(R.drawable.card_back));
            }
            (findViewById(R.id.workingMemoryTestCard)).clearAnimation();
            (findViewById(R.id.workingMemoryTestCard)).setAnimation(animation2);
            (findViewById(R.id.workingMemoryTestCard)).startAnimation(animation2);
        } else {
            // animation2 - End of 2nd half of flip
            isBackOfCardShowing=!isBackOfCardShowing;
            findViewById(R.id.workingMemoryTestCard).setEnabled(true);
            if(!isBackOfCardShowing) {
                // face card showing
                if( numberFlips > 0) {
                    flipCard = new FlipCard();
                    new Handler().postDelayed(flipCard, 3 * 1000); // flip card after 3 seconds
                } else {
                    endTest = new EndTest();
                    new Handler().postDelayed(endTest, 3 * 1000); // end test after 3 seconds if not clicked
                }
            } else {
                // back showing
                if( numberFlips > 0) {
                    flipCard = new FlipCard();
                    new Handler().postDelayed(flipCard, 1000); // flip card after 1 second
                } else {
                    endTest.deactivate(); // Clicked; deactive previous endTest
                    endTest = new EndTest();
                    new Handler().postDelayed(endTest, 1000); // end test after 1 second
                }
            }
        }
    }

    private class FlipCard implements Runnable {

        private boolean active = true;

        public void deactivate() {
            active = false;
        }

        @Override
        public void run() {
            // Flip the card
            if(active) {
                ImageView v = findViewById(R.id.workingMemoryTestCard);
                v.setEnabled(false);
                v.clearAnimation();
                v.setAnimation(animation1);
                v.startAnimation(animation1);
            }
        }
    }

    private class EndTest implements Runnable {

        private boolean active = true;

        public void deactivate() {
            active = false;
        }

        @Override
        public void run() {
            if(active) {
                findViewById(R.id.workingMemoryTestCard).setVisibility(View.INVISIBLE);
                DBApplication app = (DBApplication) getApplication();
                app.addTest(memoryTest);
                ((TextView) findViewById(R.id.workingMemoryTestResult)).setText(memoryTest.getImpairment().name());
                ((TextView) findViewById(R.id.workingMemoryTestResult)).setTextColor(getResultColor(memoryTest.getImpairment()));
                findViewById(R.id.workingMemoryButtonNextTest).setVisibility(View.VISIBLE);
                findViewById(R.id.workingMemoryTestResult).setVisibility(View.VISIBLE);
            }
        }
    }

    public void nextTest(View view) {
        Intent intent = new Intent(this, TestReportActivity.class);
        startActivity(intent);
    }

}

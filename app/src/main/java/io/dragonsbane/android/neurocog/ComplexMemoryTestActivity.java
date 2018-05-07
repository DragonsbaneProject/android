package io.dragonsbane.android.neurocog;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.dragonsbane.android.DBApplication;
import io.dragonsbane.android.R;
import io.onemfive.core.util.Numbers;
import io.onemfive.data.DID;
import io.onemfive.data.health.mental.memory.MemoryTest;

/**
 * We can also do a more complex memory like 2 back which requires more attention.
 * If I show you 6 bamboo, and then 3 wind, and then 3 wind you should not tap the screen,
 * but if the last one is 6 bamboo, you should. We could even try 3 back.
 */
public class ComplexMemoryTestActivity extends ImpairmentTestActivity {

    private int numberFlips = 12;
    private int maxNumberDifferentCards = 3;
    private int lastCardFlipped = 0;
    private int secondToLastCardFlipped = 0;
    private int currentCard = 0;
    private int currentNumberFlips = 0;
    private Long begin;
    private Long end;
    private List<Long> responseTimes = new ArrayList<>();

    private int randomStartCardIndex;

    private boolean isBackOfCardShowing = true;
    private boolean shouldNotClick = false;

    private FlipCard flipCard;
    private EndTest endTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DID did = ((DBApplication)getApplication()).getDid();
        randomStartCardIndex = Numbers.randomNumber(0, (DBApplication.cards.length-1)-maxNumberDifferentCards);
        memoryTest = MemoryTest.newInstance(BORDERLINE_IMPAIRMENT,did.getId());
        ((DBApplication)getApplication()).addActivity(ComplexMemoryTestActivity.class, this);
        setContentView(R.layout.activity_complex_memory_test);
        flipCard = new FlipCard();
        new Handler().postDelayed(flipCard, 3 * 1000); // flip card after 3 seconds
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((DBApplication)getApplication()).removeActivity(ComplexMemoryTestActivity.class);
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
        if (animation == animation1) {
            // End of 1st half of flip
            if (isBackOfCardShowing) {
                numberFlips--;
                secondToLastCardFlipped = lastCardFlipped;
                lastCardFlipped = currentCard;
                currentCard = ((DBApplication)getApplicationContext()).getRandomCard(randomStartCardIndex, randomStartCardIndex + maxNumberDifferentCards);
                shouldNotClick = currentCard != secondToLastCardFlipped;
                if(!memoryTest.cardsUsed().contains(currentCard)) memoryTest.cardsUsed().add(currentCard);
                (findViewById(R.id.complexMemoryTestCard)).setBackground(getResources().getDrawable(currentCard));
            } else {
                (findViewById(R.id.complexMemoryTestCard)).setBackground(getResources().getDrawable(R.drawable.card_back));
            }
            (findViewById(R.id.complexMemoryTestCard)).clearAnimation();
            (findViewById(R.id.complexMemoryTestCard)).setAnimation(animation2);
            (findViewById(R.id.complexMemoryTestCard)).startAnimation(animation2);
        } else {
            // animation2 - End of 2nd half of flip
            isBackOfCardShowing=!isBackOfCardShowing;
            findViewById(R.id.complexMemoryTestCard).setEnabled(true);
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
                ImageView v = findViewById(R.id.complexMemoryTestCard);
                v.setEnabled(false);
                v.clearAnimation();
                v.setAnimation(animation1);
                v.startAnimation(animation1);
                currentNumberFlips++;
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
                findViewById(R.id.complexMemoryTestCard).setVisibility(View.INVISIBLE);
                DBApplication app = (DBApplication) getApplication();
                app.addTest(memoryTest);
                ((TextView) findViewById(R.id.complexMemoryTestResult)).setText(memoryTest.getImpairment().name());
                ((TextView) findViewById(R.id.complexMemoryTestResult)).setTextColor(getResultColor(memoryTest.getImpairment()));
                findViewById(R.id.complexMemoryButtonNextTest).setVisibility(View.VISIBLE);
                findViewById(R.id.complexMemoryTestResult).setVisibility(View.VISIBLE);
            }
        }
    }

    public void nextTest(View view) {
        Intent intent = new Intent(this, WorkingMemoryTestActivity.class);
        startActivity(intent);
    }

}

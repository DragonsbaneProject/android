package io.dragonsbane.android.neurocog;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
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
 * We can use a series of tiles flipped over one at a time.
 *
 * You only tap on it if it was the one shown just before it.
 *
 * So If I show you 3 wind and the previous tile had been 6 bamboo then you do nothing.
 *
 * If the next time I show you is the 3 wind again then you tap the screen.
 *
 * This tests short term memory and, over the session, the ability to
 * retain working knowledge of the instructions.
 *
 * We can do one every 5 seconds for 1 min.
 */
public class SimpleMemoryTestActivity extends ImpairmentTestActivity {

    private int numberFlips = 12;
    private int maxNumberDifferentCards = 3;
    private int lastCardFlipped = 0;
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
        memoryTest = MemoryTest.newInstance(IMPAIRMENT,did.getId());
        ((DBApplication)getApplication()).addActivity(SimpleMemoryTestActivity.class, this);
        setContentView(R.layout.activity_simple_memory_test);
        flipCard = new FlipCard();
        new Handler().postDelayed(flipCard, 3 * 1000); // flip card after 3 seconds
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((DBApplication)getApplication()).removeActivity(SimpleMemoryTestActivity.class);
    }

    public void clickCard(View v) {
        if(isBackOfCardShowing) {memoryTest.addInappropriate();return;}
        if(shouldNotClick) {memoryTest.addNegative();return;}
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
                lastCardFlipped = currentCard;
                currentCard = ((DBApplication)getApplicationContext()).getRandomCard(randomStartCardIndex, randomStartCardIndex + maxNumberDifferentCards);
                shouldNotClick = currentCard != lastCardFlipped;
                if(!memoryTest.cardsUsed().contains(currentCard)) memoryTest.cardsUsed().add(currentCard);
                (findViewById(R.id.simpleMemoryTestCard)).setBackground(getResources().getDrawable(currentCard));
            } else {
                (findViewById(R.id.simpleMemoryTestCard)).setBackground(getResources().getDrawable(R.drawable.card_back));
            }
            (findViewById(R.id.simpleMemoryTestCard)).clearAnimation();
            (findViewById(R.id.simpleMemoryTestCard)).setAnimation(animation2);
            (findViewById(R.id.simpleMemoryTestCard)).startAnimation(animation2);
        } else {
            // animation2 - End of 2nd half of flip
            isBackOfCardShowing=!isBackOfCardShowing;
            findViewById(R.id.simpleMemoryTestCard).setEnabled(true);
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
                ImageView v = findViewById(R.id.simpleMemoryTestCard);
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
                findViewById(R.id.simpleMemoryTestCard).setVisibility(View.INVISIBLE);
                DBApplication app = (DBApplication) getApplication();
                app.addTest(memoryTest);
                ((TextView) findViewById(R.id.simpleMemoryTestResult)).setText(memoryTest.getImpairment().name());
                ((TextView) findViewById(R.id.simpleMemoryTestResult)).setTextColor(getResultColor(memoryTest.getImpairment()));
                findViewById(R.id.simpleMemoryButtonNextTest).setVisibility(View.VISIBLE);
                findViewById(R.id.simpleMemoryTestResult).setVisibility(View.VISIBLE);
            }
        }
    }

    public void nextTest(View view) {
        Intent intent = new Intent(this, ComplexMemoryTestActivity.class);
        startActivity(intent);
    }

}

package io.dragonsbane.neurocog.tests;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;

import io.dragonsbane.neurocog.DBApplication;
import io.dragonsbane.neurocog.R;
import io.onemfive.core.util.Numbers;

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

    private int randomStartCardIndex;

    private boolean isBackOfCardShowing = true;
    private boolean shouldNotClick = false;

    private FlipCard flipCard;
    private EndTest endTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        randomStartCardIndex = Numbers.randomNumber(0, (DBApplication.cards.length-1)-maxNumberDifferentCards);
        test = new ImpairmentTest(did, IMPAIRMENT);
        test.setBloodAlcoholContent(bac);
        setContentView(R.layout.activity_simple_memory_test);

        Toolbar toolbar = findViewById(R.id.action_bar);
        TextView titleTextView = (TextView) toolbar.getChildAt(0);
        titleTextView.setTextColor(getResources().getColor(R.color.dragonsbaneBlack));
        titleTextView.setTypeface(((DBApplication)getApplication()).getNexaBold());

        flipCard = new FlipCard();
        new Handler().postDelayed(flipCard, normalFlipDurationMs); // flip card after 3 seconds
    }

    public void clickCard(View v) {
        end = new Date().getTime();
        test.setTimeEnded(end);
        long diff = end - begin;
        if(isBackOfCardShowing) {
            test.addInappropriate(diff);
            return;
        }
        if(shouldNotClick) {
            test.addNegative(diff);
            return;
        }
        test.addSuccess(diff);

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
            test.addMiss(normalFlipDurationMs);
        }
        if (animation == animation2) {
            if(isBackOfCardShowing) {
                begin = new Date().getTime();
                test.setTimeStarted(begin);
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
                if(!test.cardsUsed.contains(currentCard)) test.cardsUsed.add(currentCard);
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
                    new Handler().postDelayed(flipCard, normalFlipDurationMs); // flip card after 3 seconds
                } else {
                    endTest = new EndTest();
                    new Handler().postDelayed(endTest, normalFlipDurationMs); // end test after 3 seconds if not clicked
                }
            } else {
                // back showing
                if( numberFlips > 0) {
                    flipCard = new FlipCard();
                    new Handler().postDelayed(flipCard, shortFlipDuractionMs); // flip card after 1 second
                } else {
                    endTest.deactivate(); // Clicked; deactive previous endTest
                    endTest = new EndTest();
                    new Handler().postDelayed(endTest, shortFlipDuractionMs); // end test after 1 second
                }
            }
        }
    }

    private class FlipCard implements Runnable {

        private boolean active = true;

        void deactivate() {
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

        void deactivate() {
            active = false;
        }

        @Override
        public void run() {
            if(active) {
                findViewById(R.id.simpleMemoryTestCard).setVisibility(View.INVISIBLE);
                testFinished();
                findViewById(R.id.simpleMemoryButtonNextTest).setVisibility(View.VISIBLE);
                findViewById(R.id.resultsLayout).setVisibility(View.VISIBLE);

                // Successes
                ((TextView)findViewById(R.id.resultsTotalSuccess)).setText(String.valueOf(test.getSuccesses()));
                ((TextView)findViewById(R.id.resultsMinSuccess)).setText(String.valueOf(test.getMinResponseTimeSuccessMs()));
                ((TextView)findViewById(R.id.resultsMaxSuccess)).setText(String.valueOf(test.getMaxResponseTimeSuccessMs()));
                ((TextView)findViewById(R.id.resultsAvgSuccess)).setText(String.valueOf(test.getAvgResponseTimeSuccessMs()));
                // Misses
                ((TextView)findViewById(R.id.resultsTotalMisses)).setText(String.valueOf(test.getMisses()));
                ((TextView)findViewById(R.id.resultsMinMisses)).setText(String.valueOf(test.getMinResponseTimeMissMs()));
                ((TextView)findViewById(R.id.resultsMaxMisses)).setText(String.valueOf(test.getMaxResponseTimeMissMs()));
                ((TextView)findViewById(R.id.resultsAvgMisses)).setText(String.valueOf(test.getAvgResponseTimeMissMs()));
                // Negative
                ((TextView)findViewById(R.id.resultsTotalNegative)).setText(String.valueOf(test.getNegatives()));
                ((TextView)findViewById(R.id.resultsMinNegative)).setText(String.valueOf(test.getMinResponseTimeNegativeMs()));
                ((TextView)findViewById(R.id.resultsMaxNegative)).setText(String.valueOf(test.getMaxResponseTimeNegativeMs()));
                ((TextView)findViewById(R.id.resultsAvgNegative)).setText(String.valueOf(test.getAvgResponseTimeNegativeMs()));
                // Inappropriate
                ((TextView)findViewById(R.id.resultsTotalInappropriate)).setText(String.valueOf(test.getInappropriates()));
                ((TextView)findViewById(R.id.resultsMinInappropriate)).setText(String.valueOf(test.getMinResponseTimeInappropriateMs()));
                ((TextView)findViewById(R.id.resultsMaxInappropriate)).setText(String.valueOf(test.getMaxResponseTimeInappropriateMs()));
                ((TextView)findViewById(R.id.resultsAvgInappropriate)).setText(String.valueOf(test.getAvgResponseTimeInappropriateMs()));
            }
        }
    }

    public void nextTest(View view) {
        Intent intent = new Intent(this, ComplexMemoryTestActivity.class);
        startActivity(intent);
    }

}

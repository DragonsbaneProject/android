package io.dragonsbane.neurocog.tests;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import io.dragonsbane.data.ImpairmentTest;
import io.dragonsbane.neurocog.DBApplication;
import io.dragonsbane.neurocog.MainActivity;
import io.dragonsbane.neurocog.R;
import io.onemfive.core.util.Numbers;

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

    private boolean isBackOfCardShowing = true;
    private boolean shouldNotClick = false;

    private FlipCard flipCard;
    private EndTest endTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        test = new ImpairmentTest(did, NO_IMPAIRMENT);
        test.setBloodAlcoholContent(bac);
        setContentView(R.layout.activity_working_memory_test);

        Toolbar toolbar = findViewById(R.id.action_bar);
        TextView titleTextView = (TextView) toolbar.getChildAt(0);
        titleTextView.setTextColor(getResources().getColor(R.color.dragonsbaneBlack));
        titleTextView.setTypeface(((DBApplication)getApplication()).getNexaBold());

        List<ImpairmentTest> tests = ((DBApplication)getApplication()).getTests();
        for(ImpairmentTest test : tests) {
            cardsUsedPrior.addAll(test.getCardsUsed());
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
        new Handler().postDelayed(flipCard, normalFlipDurationMs); // flip card after 3 seconds
    }

    public void clickCard(View v) {
        end = new Date().getTime();
        test.setTimeEnded(end);
        long diff = end - begin;
        if(isBackOfCardShowing) {
            test.addInappropriate(diff);return;}
        if(shouldNotClick) {
            test.addMiss(diff);return;}
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
        shouldNotClick = new Random().nextBoolean();
        if (animation == animation1) {
            // End of 1st half of flip
            if (isBackOfCardShowing) {
                numberFlips--;
                currentNumberFlips++;
                if(shouldNotClick) {
                    int random = Numbers.randomNumber(0,cardsNotUsedPrior.size()-1);
                    currentCard = cardsNotUsedPrior.get(random);
                } else {
                    int random = Numbers.randomNumber(0,cardsUsedPrior.size()-1);
                    currentCard = cardsUsedPrior.get(random);
                }
                if(!test.getCardsUsed().contains(currentCard)) test.getCardsUsed().add(currentCard);
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
                findViewById(R.id.workingMemoryButtonNextTest).setVisibility(View.VISIBLE);
                findViewById(R.id.resultsLayout).setVisibility(View.VISIBLE);

                // Successes
                ((TextView)findViewById(R.id.resultsTotalSuccess)).setText(String.valueOf(test.getSuccesses().size()));
                ((TextView)findViewById(R.id.resultsMinSuccess)).setText(String.valueOf(test.getMinResponseTimeSuccessMs()));
                ((TextView)findViewById(R.id.resultsMaxSuccess)).setText(String.valueOf(test.getMaxResponseTimeSuccessMs()));
                ((TextView)findViewById(R.id.resultsAvgSuccess)).setText(String.valueOf(test.getAvgResponseTimeSuccessMs()));
                // Misses
                ((TextView)findViewById(R.id.resultsTotalMisses)).setText(String.valueOf(test.getMisses().size()));
                ((TextView)findViewById(R.id.resultsMinMisses)).setText(String.valueOf(test.getMinResponseTimeMissMs()));
                ((TextView)findViewById(R.id.resultsMaxMisses)).setText(String.valueOf(test.getMaxResponseTimeMissMs()));
                ((TextView)findViewById(R.id.resultsAvgMisses)).setText(String.valueOf(test.getAvgResponseTimeMissMs()));
                // Negative
                ((TextView)findViewById(R.id.resultsTotalNegative)).setText(String.valueOf(test.getNegatives().size()));
                ((TextView)findViewById(R.id.resultsMinNegative)).setText(String.valueOf(test.getMinResponseTimeNegativeMs()));
                ((TextView)findViewById(R.id.resultsMaxNegative)).setText(String.valueOf(test.getMaxResponseTimeNegativeMs()));
                ((TextView)findViewById(R.id.resultsAvgNegative)).setText(String.valueOf(test.getAvgResponseTimeNegativeMs()));
                // Inappropriate
                ((TextView)findViewById(R.id.resultsTotalInappropriate)).setText(String.valueOf(test.getInappropriates().size()));
                ((TextView)findViewById(R.id.resultsMinInappropriate)).setText(String.valueOf(test.getMinResponseTimeInappropriateMs()));
                ((TextView)findViewById(R.id.resultsMaxInappropriate)).setText(String.valueOf(test.getMaxResponseTimeInappropriateMs()));
                ((TextView)findViewById(R.id.resultsAvgInappropriate)).setText(String.valueOf(test.getAvgResponseTimeInappropriateMs()));
            }
        }
    }

    public void nextTest(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}

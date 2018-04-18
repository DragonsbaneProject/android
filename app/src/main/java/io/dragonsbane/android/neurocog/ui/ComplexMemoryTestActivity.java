package io.dragonsbane.android.neurocog.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import io.dragonsbane.android.DBApplication;
import io.dragonsbane.android.R;
import io.dragonsbane.android.neurocog.models.ComplexMemoryImpairmentModel;
import io.dragonsbane.android.neurocog.tests.ComplexMemoryTest;
import io.dragonsbane.android.util.Numbers;
import io.onemfive.data.health.TestResult;

/**
 * We can also do a more complex memory like 2 back which requires more attention.
 * If I show you 6 bamboo, and then 3 wind, and then 3 wind you should not tap the screen,
 * but if the last one is 6 bamboo, you should. We could even try 3 back.
 */
public class ComplexMemoryTestActivity extends AppCompatActivity implements Animation.AnimationListener {

    private ComplexMemoryTest test;
    private ComplexMemoryImpairmentModel model;

    private int numberFlips = 12;
    private int maxNumberDifferentCards = 3;
    private int lastCardFlipped = 0;
    private int secondToLastCardFlipped = 0;
    private int currentCard = 0;

    private int randomStartCardIndex;

    private Animation animation1;
    private Animation animation2;

    private boolean isBackOfCardShowing = true;
    private boolean shouldNotClick = false;

    private FlipCard flipCard;
    private EndTest endTest;

    public ComplexMemoryTestActivity() {
        randomStartCardIndex = Numbers.randomNumber(0, 51-maxNumberDifferentCards);
        test = new ComplexMemoryTest();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        test = new ComplexMemoryTest();
        model = new ComplexMemoryImpairmentModel();
        ((DBApplication)getApplication()).addActivity(ComplexMemoryTestActivity.class, this);
        setContentView(R.layout.activity_complex_memory_test);
        animation1 = AnimationUtils.loadAnimation(this, R.anim.to_middle);
        animation1.setAnimationListener(this);
        animation2 = AnimationUtils.loadAnimation(this, R.anim.from_middle);
        animation2.setAnimationListener(this);

        flipCard = new FlipCard();
        new Handler().postDelayed(flipCard, 3 * 1000); // flip card after 3 seconds
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((DBApplication)getApplication()).removeActivity(ComplexMemoryTestActivity.class);
    }

    public void clickCard(View v) {
        if(isBackOfCardShowing) {test.inappropriate++;return;}
        if(shouldNotClick) {test.negative++;return;}
        test.successes++;
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
            test.misses++;
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
                if(!test.cardsUsed.contains(currentCard)) test.cardsUsed.add(currentCard);
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
                model.evaluate(test);
                TestResult result = test.getTestResult();
                if (result.getScore() < 80) {
                    ((TextView) findViewById(R.id.complexMemoryTestResult)).setText(getResources().getText(R.string.testPassed));
                    ((TextView) findViewById(R.id.complexMemoryTestResult)).setTextColor(getResources().getColor(R.color.colorGrassGreen));
                    findViewById(R.id.complexMemoryButtonNextTest).setVisibility(View.VISIBLE);
                } else {
                    ((TextView) findViewById(R.id.complexMemoryTestResult)).setText(getResources().getText(R.string.testFailed));
                    ((TextView) findViewById(R.id.complexMemoryTestResult)).setTextColor(getResources().getColor(R.color.colorWarning));
                }
                findViewById(R.id.complexMemoryTestResult).setVisibility(View.VISIBLE);
                DBApplication app = (DBApplication) getApplication();
                app.addTest(test);
            }
        }
    }

    public void nextTest(View view) {
        Intent intent = new Intent(this, WorkingMemoryTestActivity.class);
        startActivity(intent);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}

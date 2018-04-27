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
import io.dragonsbane.android.neurocog.TestResult;
import io.dragonsbane.android.neurocog.models.SimpleMemoryImpairmentModel;
import io.dragonsbane.android.neurocog.tests.SimpleMemoryTest;
import io.dragonsbane.android.util.Numbers;

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
public class SimpleMemoryTestActivity extends AppCompatActivity implements Animation.AnimationListener {

    private SimpleMemoryTest test;
    private SimpleMemoryImpairmentModel model;

    private int numberFlips = 12;
    private int maxNumberDifferentCards = 3;
    private int lastCardFlipped = 0;
    private int currentCard = 0;

    private int randomStartCardIndex;

    private Animation animation1;
    private Animation animation2;

    private boolean isBackOfCardShowing = true;
    private boolean shouldNotClick = false;

    private FlipCard flipCard;
    private EndTest endTest;

    public SimpleMemoryTestActivity() {
        randomStartCardIndex = Numbers.randomNumber(0, 51-maxNumberDifferentCards);
        test = new SimpleMemoryTest();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        test = new SimpleMemoryTest();
        model = new SimpleMemoryImpairmentModel();
        ((DBApplication)getApplication()).addActivity(SimpleMemoryTestActivity.class, this);
        setContentView(R.layout.activity_simple_memory_test);
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
        ((DBApplication)getApplication()).removeActivity(SimpleMemoryTestActivity.class);
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
                model.evaluate(test);
                TestResult result = test.getTestResult();
                if (result.getScore() < 80) {
                    ((TextView) findViewById(R.id.simpleMemoryTestResult)).setText(getResources().getText(R.string.testPassed));
                    ((TextView) findViewById(R.id.simpleMemoryTestResult)).setTextColor(getResources().getColor(R.color.colorGrassGreen));
                    findViewById(R.id.simpleMemoryButtonNextTest).setVisibility(View.VISIBLE);
                } else {
                    ((TextView) findViewById(R.id.simpleMemoryTestResult)).setText(getResources().getText(R.string.testFailed));
                    ((TextView) findViewById(R.id.simpleMemoryTestResult)).setTextColor(getResources().getColor(R.color.colorWarning));
                }
                findViewById(R.id.simpleMemoryTestResult).setVisibility(View.VISIBLE);
                DBApplication app = (DBApplication) getApplication();
                app.addTest(test);
            }
        }
    }

    public void nextTest(View view) {
        Intent intent = new Intent(this, ComplexMemoryTestActivity.class);
        startActivity(intent);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}

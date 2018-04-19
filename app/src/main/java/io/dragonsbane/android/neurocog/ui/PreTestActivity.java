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
import io.dragonsbane.android.neurocog.models.PreTestImpairmentModel;
import io.dragonsbane.android.neurocog.tests.PreTestTest;
import io.dragonsbane.android.util.Numbers;
import io.onemfive.data.TestResult;

/**
 * First task is to prove hardware is working right and the person understands the instructions.
 * To do this we’ll do a simple test using mahjong tiles.
 *
 * The person is asked to tap the screen when the tile turns over to show its face,
 * and this act turns it back over.
 *
 * We can do 5 flips in 30 seconds, not evenly spaced in time, using just one tile.
 *
 * We’d score how many times the person tapped the screen appropriately
 * and how many inappropriate taps there were.
 *
 * Defining appropriate and inappropriate needs to happen through modeling and verification.
 *
 * This one is so simple that if they don’t have a perfect score,
 * something is wrong that the test can’t be done
 * (screen broken, person very drunk, person has debilitating dementia, etc).
 */
public class PreTestActivity extends AppCompatActivity implements Animation.AnimationListener {

    private PreTestTest test;
    private PreTestImpairmentModel model;
    private int numberFlips = 5;

    private Animation animation1;
    private Animation animation2;

    private boolean isBackOfCardShowing = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        test = new PreTestTest();
        model = new PreTestImpairmentModel();
        ((DBApplication)getApplication()).addActivity(PreTestActivity.class, this);
        setContentView(R.layout.activity_pre_test);
        animation1 = AnimationUtils.loadAnimation(this, R.anim.to_middle);
        animation1.setAnimationListener(this);
        animation2 = AnimationUtils.loadAnimation(this, R.anim.from_middle);
        animation2.setAnimationListener(this);

        new Handler().postDelayed(new FlipCard(), 3 * 1000); // flip card after 3 seconds
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((DBApplication)getApplication()).removeActivity(PreTestActivity.class);
    }

    public void clickCard(View v) {
        if(isBackOfCardShowing) {
            test.inappropriate++;
            return;
        }
        test.clickedCard();
        v.setEnabled(false);
        v.clearAnimation();
        v.setAnimation(animation1);
        v.startAnimation(animation1);
        if(numberFlips > 0)
            new Handler().postDelayed(new FlipCard(), Numbers.randomNumber(1, 3) * 1000); // flip card after random wait between 1 and 3 seconds
        else {
            new Handler().postDelayed(new EndTest(), 1000); // end test after 1 second
        }
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (animation == animation1) {
            if (isBackOfCardShowing) {
                (findViewById(R.id.preTestCard)).setBackground(getResources().getDrawable(R.drawable.card_ca));
            } else {
                (findViewById(R.id.preTestCard)).setBackground(getResources().getDrawable(R.drawable.card_back));
            }
            (findViewById(R.id.preTestCard)).clearAnimation();
            (findViewById(R.id.preTestCard)).setAnimation(animation2);
            (findViewById(R.id.preTestCard)).startAnimation(animation2);
        } else {
            // animation2
            isBackOfCardShowing=!isBackOfCardShowing;
            findViewById(R.id.preTestCard).setEnabled(true);
            if(!isBackOfCardShowing) {
                test.flippedCard();
            }
        }
    }

    private class FlipCard implements Runnable {

        @Override
        public void run() {
            // Flip the card
            ImageView v = findViewById(R.id.preTestCard);
            v.clearAnimation();
            v.setAnimation(animation1);
            v.startAnimation(animation1);
            numberFlips--;
        }
    }

    private class EndTest implements Runnable {

        @Override
        public void run() {
            findViewById(R.id.preTestCard).setVisibility(View.INVISIBLE);
            model.evaluate(test);
            DBApplication app = (DBApplication) getApplication();
            app.addTest(test);
            TestResult result = test.getTestResult();
            if(result.getScore() < 80) {
                ((TextView)findViewById(R.id.preTestResult)).setText(getResources().getText(R.string.testPassed));
                ((TextView)findViewById(R.id.preTestResult)).setTextColor(getResources().getColor(R.color.colorGrassGreen));
                findViewById(R.id.preTestButtonNextTest).setVisibility(View.VISIBLE);
            } else {
                ((TextView)findViewById(R.id.preTestResult)).setText(getResources().getText(R.string.testFailed));
                ((TextView)findViewById(R.id.preTestResult)).setTextColor(getResources().getColor(R.color.colorWarning));
            }
            ((TextView)findViewById(R.id.preTestMSBetween1stFlippedAndClickedScore)).setText(String.valueOf(test.msBetween1stFlippedAndClicked));
            ((TextView)findViewById(R.id.preTestMSBetween2ndFlippedAndClickedScore)).setText(String.valueOf(test.msBetween2ndFlippedAndClicked));
            ((TextView)findViewById(R.id.preTestMSBetween3rdFlippedAndClickedScore)).setText(String.valueOf(test.msBetween3rdFlippedAndClicked));
            ((TextView)findViewById(R.id.preTestMSBetween4thFlippedAndClickedScore)).setText(String.valueOf(test.msBetween4thFlippedAndClicked));
            ((TextView)findViewById(R.id.preTestMSBetween5thFlippedAndClickedScore)).setText(String.valueOf(test.msBetween5thFlippedAndClicked));
            findViewById(R.id.preTestLayout).setVisibility(View.VISIBLE);
        }
    }

    public void nextTest(View view) {
        Intent intent = new Intent(this, SimpleMemoryTestActivity.class);
        startActivity(intent);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

}

package io.dragonsbane.android.neurocog;

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

import io.dragonsbane.android.DBApplication;
import io.dragonsbane.android.R;
import io.onemfive.android.api.healthcare.HealthRecordAPI;
import io.onemfive.core.util.Numbers;
import io.onemfive.data.health.mental.memory.MemoryTest;

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
 *
 * If they hit off the card, they should get a warning saying to only click the card.
 *
 */
public class PreTestActivity extends ImpairmentTestActivity {

    private int numberFlips = 5;
    private int currentNumberFlips = 0;
    private Long begin;
    private Long end;
    private List<Long> responseTimes = new ArrayList<>();

    private boolean isBackOfCardShowing = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        memoryTest = MemoryTest.newInstance(GROSS_IMPAIRMENT, did.getId());
        memoryTest.setBloodAlcoholContent(bac);
        app.addActivity(PreTestActivity.class, this);
        // Ensure empty test list
        app.getTests().clear();
        setContentView(R.layout.activity_pre_test);

        Toolbar toolbar = findViewById(R.id.action_bar);
        TextView titleTextView = (TextView) toolbar.getChildAt(0);
        titleTextView.setTextColor(getResources().getColor(R.color.dragonsbaneBlack));
        titleTextView.setTypeface(((DBApplication)getApplication()).getNexaBold());

        new Handler().postDelayed(new FlipCard(), 3 * 1000); // flip card after 3 seconds
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        app.removeActivity(PreTestActivity.class);
    }

    public void clickCard(View v) {
        if(isBackOfCardShowing) {
            memoryTest.addInappropriate();
            return;
        }
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
    public void onAnimationStart(Animation animation) {
        if (animation == animation2) {
            if(isBackOfCardShowing) {
                findViewById(R.id.preTestCard).setEnabled(true);
                begin = new Date().getTime();
            }
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
            currentNumberFlips++;
        }
    }

    private class EndTest implements Runnable {

        @Override
        public void run() {
            findViewById(R.id.preTestCard).setVisibility(View.INVISIBLE);
            HealthRecordAPI.saveMemoryTest(getApplicationContext(), did, memoryTest);
            app.addTest(memoryTest);
            findViewById(R.id.preTestResultDescription).setVisibility(View.VISIBLE);
            findViewById(R.id.preTestButtonNextTest).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.preTestMSBetween1stFlippedAndClickedScore)).setText(String.valueOf(responseTimes.get(0)));
            ((TextView)findViewById(R.id.preTestMSBetween2ndFlippedAndClickedScore)).setText(String.valueOf(responseTimes.get(1)));
            ((TextView)findViewById(R.id.preTestMSBetween3rdFlippedAndClickedScore)).setText(String.valueOf(responseTimes.get(2)));
            ((TextView)findViewById(R.id.preTestMSBetween4thFlippedAndClickedScore)).setText(String.valueOf(responseTimes.get(3)));
            ((TextView)findViewById(R.id.preTestMSBetween5thFlippedAndClickedScore)).setText(String.valueOf(responseTimes.get(4)));
            ((TextView)findViewById(R.id.preTestMSAverageScore)).setText(String.valueOf(memoryTest.getAvgResponseTimeMs()));
            findViewById(R.id.preTestLayout).setVisibility(View.VISIBLE);
        }
    }

    public void nextTest(View view) {
        Intent intent = new Intent(this, SimpleMemoryTestActivity.class);
        startActivity(intent);
    }

}

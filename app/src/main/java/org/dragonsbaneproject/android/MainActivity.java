package org.dragonsbaneproject.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private UIController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ((DBApplication)getApplication()).addActivity(MainActivity.class,this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        ((DBApplication)getApplication()).removeActivity(MainActivity.class);
    }

    public void start(View view) {

    }
}

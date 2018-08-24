package io.dragonsbane.android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import io.dragonsbane.android.R;

public class SettingsActivity extends DBActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }
}

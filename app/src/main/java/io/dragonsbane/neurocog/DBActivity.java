package io.dragonsbane.neurocog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import io.dragonsbane.neurocog.database.Storage;

public abstract class DBActivity extends AppCompatActivity {

    protected Storage storage;

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((DBApplication)getApplication()).addActivity(this.getClass(), this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((DBApplication)getApplication()).removeActivity(this.getClass());
    }
}

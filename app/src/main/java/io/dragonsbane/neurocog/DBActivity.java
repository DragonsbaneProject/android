package io.dragonsbane.neurocog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import io.dragonsbane.neurocog.database.StorageDML;

public abstract class DBActivity extends AppCompatActivity {

    protected StorageDML storageDML;

    public void setStorageDML(StorageDML storageDML) {
        this.storageDML = storageDML;
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

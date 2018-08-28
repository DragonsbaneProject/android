package io.dragonsbane.neurocog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import io.dragonsbane.neurocog.database.SQLiteInfoVaultDB;

public abstract class DBActivity extends AppCompatActivity {

    protected SQLiteInfoVaultDB db;

    public void setDB(SQLiteInfoVaultDB db) {
        this.db = db;
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

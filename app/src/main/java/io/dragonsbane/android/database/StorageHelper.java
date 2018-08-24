package io.dragonsbane.android.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class StorageHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "dgb";
    private static final String DATABASE_CREATE = "create table ImpairmentTest( id integer primary key, name text not null);";

    public StorageHelper(Context context, int version) {
        super(context, DB_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    /**
     * TODO: create an upgrade path without losing data
     * @param database
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.w(StorageHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS ImpairmentTest");
        onCreate(database);
    }

}
package io.dragonsbane.neurocog.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class StorageHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "dgb2";
    private static final String DATABASE_CREATE =
            "create table ImpairmentTest ( " +
            "id TEXT primary key, " +
            "tester TEXT not null, " +
            "name TEXT not null, " +
            "baseline INTEGER not null, " +
            "timeStarted INTEGER not null, " +
            "timeEnded INTEGER not null, " +
            "bloodAlcoholContent REAL not null, " +
            "cardsUsed TEXT not null, " +
            "successes INTEGER not null, " +
            "minResponseTimeSuccessMs INTEGER not null, " +
            "maxResponseTimeSuccessMs INTEGER not null, " +
            "avgResponseTimeSuccessMs INTEGER not null, " +
            "misses INTEGER not null, " +
            "minResponseTimeMissMs INTEGER not null, " +
            "maxResponseTimeMissMs INTEGER not null, " +
            "avgResponseTimeMissMs INTEGER not null, " +
            "inappropriates INTEGER not null, " +
            "minResponseTimeInappropriateMs INTEGER not null, " +
            "maxResponseTimeInappropriateMs INTEGER not null, " +
            "avgResponseTimeInappropriateMs INTEGER not null, " +
            "negatives INTEGER not null, " +
            "minResponseTimeNegativeMs INTEGER not null, " +
            "maxResponseTimeNegativeMs INTEGER not null, " +
            "avgResponseTimeNegativeMs INTEGER not null " +
            ");";

    public StorageHelper(Context context, int version) {
        super(context, DB_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL("DROP TABLE IF EXISTS ImpairmentTest");
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

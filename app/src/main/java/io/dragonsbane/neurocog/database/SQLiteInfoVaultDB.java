package io.dragonsbane.neurocog.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Properties;

import io.onemfive.core.infovault.DAO;
import io.onemfive.core.infovault.InfoVaultDB;

public class SQLiteInfoVaultDB extends SQLiteOpenHelper implements InfoVaultDB {

    private static final String DB_NAME = "dgb2";
    private static final String DB_IMPAIRMENT_TEST_CREATE =
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

    public SQLiteInfoVaultDB(Context context, int version) {
        super(context, DB_NAME, null, version);
    }

    public SQLiteDatabase getDatabase() {
        return getWritableDatabase();
    }

    @Override
    public void execute(DAO dao) throws Exception {

    }

    @Override
    public Status getStatus() {
        return null;
    }

    @Override
    public boolean init(Properties properties) {
        return false;
    }

    @Override
    public boolean teardown() {
        return false;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL("DROP TABLE IF EXISTS ImpairmentTest");
        database.execSQL(DB_IMPAIRMENT_TEST_CREATE);
    }

    /**
     * TODO: create an upgrade path without losing data
     * @param db SQLiteDatabase
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(SQLiteInfoVaultDB.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        // Begin
        db.execSQL("PRAGMA foreign_keys=off;");
        db.execSQL("BEGIN TRANSACTION;");
        // Messages
        db.execSQL("ALTER TABLE ImpairmentTest RENAME TO ImpairmentTest_temp;");
        db.execSQL(DB_IMPAIRMENT_TEST_CREATE);
        db.execSQL("INSERT INTO ImpairmentTest SELECT * FROM ImpairmentTest_temp;");
        db.execSQL("DROP TABLE ImpairmentTest_temp");
        // Commit
        db.execSQL("COMMIT;");
        db.execSQL("PRAGMA foreign_keys=on;");
        onCreate(db);
    }

}

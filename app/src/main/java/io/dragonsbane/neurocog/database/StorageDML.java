package io.dragonsbane.neurocog.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import io.dragonsbane.neurocog.tests.ImpairmentTest;
import io.onemfive.data.util.JSONParser;

public class StorageDML {

    private StorageDDL helper;
    private SQLiteDatabase db;

    public StorageDML(Context context, int version) {
        helper = new StorageDDL(context, version);
        db = helper.getWritableDatabase();
    }

    public void saveImpairmentTest(ImpairmentTest t) {
        Log.i(StorageDDL.class.getName(),"Saving ImpairmentTest...");
        ContentValues v = new ContentValues();
        v.put("name",t.getName());
        v.put("baseline",t.getBaseline());
        v.put("bloodAlcoholContent",t.getBloodAlcoholContent());
        v.put("cardsUsed", JSONParser.toString(t.getCardsUsed()));
        v.put("tester",t.getDid().getIdentityHash());
        v.put("timeStarted",t.getTimeStarted());
        v.put("timeEnded",t.getTimeEnded());
        v.put("successes",t.getSuccesses());
        v.put("minResponseTimeSuccessMs",t.getMinResponseTimeSuccessMs());
        v.put("maxResponseTimeSuccessMs",t.getMaxResponseTimeSuccessMs());
        v.put("avgResponseTimeSuccessMs",t.getAvgResponseTimeSuccessMs());
        v.put("inappropriates",t.getInappropriates());
        v.put("minResponseTimeInappropriateMs",t.getMinResponseTimeInappropriateMs());
        v.put("maxResponseTimeInappropriateMs",t.getMaxResponseTimeInappropriateMs());
        v.put("avgResponseTimeInappropriateMs",t.getAvgResponseTimeInappropriateMs());
        v.put("negatives",t.getNegatives());
        v.put("minResponseTimeNegativeMs",t.getMinResponseTimeNegativeMs());
        v.put("maxResponseTimeNegativeMs",t.getMaxResponseTimeNegativeMs());
        v.put("avgResponseTimeNegativeMs",t.getAvgResponseTimeNegativeMs());
        v.put("misses",t.getMisses());
        v.put("minResponseTimeMissMs",t.getMinResponseTimeMissMs());
        v.put("maxResponseTimeMissMs",t.getMaxResponseTimeMissMs());
        v.put("avgResponseTimeMissMs",t.getAvgResponseTimeMissMs());
        db.beginTransaction();;
        db.insert(ImpairmentTest.class.getSimpleName(),null,v);
        db.endTransaction();
    }
}

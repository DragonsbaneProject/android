package io.dragonsbane.android.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import io.dragonsbane.android.neurocog.ImpairmentTest;

public class Storage {

    private StorageHelper helper;
    private SQLiteDatabase db;

    public Storage(Context context, int version) {
        helper = new StorageHelper(context, version);
        db = helper.getWritableDatabase();
    }

    public void saveImpairmentTest(ImpairmentTest t) {
        Log.i(StorageHelper.class.getName(),"Saving ImpairmentTest...");
        ContentValues v = new ContentValues();
        v.put("name",t.getName());
        v.put("baseline",t.getBaseline());
        v.put("bac",t.getBloodAlcoholContent());
        v.put("cardsUsed",t.getCardsUsed().toString());
        v.put("author.identityHash",t.getDid().getIdentityHash());
        v.put("successes",t.getSuccesses());
        v.put("minResponseTimeSuccessMs",t.getMinResponseTimeSuccessMs());
        v.put("maxResponseTimeSuccessMs",t.getMaxResponseTimeSuccessMs());
        v.put("avgResponseTimeSuccessMs",t.getAvgResponseTimeSuccessMs());
        v.put("inappropriates",t.getInappropriates());
        v.put("minResponseTimeInappropriateMs",t.getMinResponseTimeInappropriateMs());
        v.put("maxResponseTimeInappropriateMs",t.getMaxResponseTimeInappropriateMs());
        v.put("avgResponseTimeInappropriateMs",t.getAvgResponseTimeInappropriateMs());
        v.put("negatives",t.getNegatives());
        v.put("minResponseTimeNegativesMs",t.getMinResponseTimeNegativeMs());
        v.put("maxResponseTimeNegativesMs",t.getMaxResponseTimeNegativeMs());
        v.put("avgResponseTimeNegativesMs",t.getAvgResponseTimeNegativeMs());
        v.put("misses",t.getMisses());
        v.put("minResponseTimeMissMs",t.getMinResponseTimeMissMs());
        v.put("maxResponseTimeMissMs",t.getMaxResponseTimeMissMs());
        v.put("avgResponseTimeMissMs",t.getAvgResponseTimeMissMs());
        db.beginTransaction();;
        db.insert(ImpairmentTest.class.getSimpleName(),null,v);
        db.endTransaction();
    }
}

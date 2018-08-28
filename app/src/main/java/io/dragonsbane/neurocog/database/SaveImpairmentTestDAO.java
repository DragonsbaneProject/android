package io.dragonsbane.neurocog.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import io.dragonsbane.data.ImpairmentTest;
import io.onemfive.core.infovault.BaseDAO;
import io.onemfive.core.infovault.InfoVaultDB;
import io.onemfive.data.util.JSONParser;

public class SaveImpairmentTestDAO extends BaseDAO {

    private ImpairmentTest t;
    private SQLiteDatabase db;

    public SaveImpairmentTestDAO(InfoVaultDB infoVaultDB, ImpairmentTest t) {
        super(infoVaultDB);
        db = ((SQLiteInfoVaultDB) infoVaultDB).getDatabase();
        this.t = t;
    }

    @Override
    public void execute() throws Exception {
        Log.i(SaveImpairmentTestDAO.class.getName(),"Saving ImpairmentTest...");
        ContentValues v = new ContentValues();
        v.put("name",t.getName());
        v.put("baseline",t.getBaseline());
        v.put("bloodAlcoholContent",t.getBloodAlcoholContent());
        v.put("cardsUsed", JSONParser.toString(t.getCardsUsed()));
        v.put("tester",t.getDid().getIdentityHash());
        v.put("timeStarted",t.getTimeStarted());
        v.put("timeEnded",t.getTimeEnded());
        v.put("successes",JSONParser.toString(t.getSuccesses()));
        v.put("minResponseTimeSuccessMs",t.getMinResponseTimeSuccessMs());
        v.put("maxResponseTimeSuccessMs",t.getMaxResponseTimeSuccessMs());
        v.put("avgResponseTimeSuccessMs",t.getAvgResponseTimeSuccessMs());
        v.put("inappropriates",JSONParser.toString(t.getInappropriates()));
        v.put("minResponseTimeInappropriateMs",t.getMinResponseTimeInappropriateMs());
        v.put("maxResponseTimeInappropriateMs",t.getMaxResponseTimeInappropriateMs());
        v.put("avgResponseTimeInappropriateMs",t.getAvgResponseTimeInappropriateMs());
        v.put("negatives",JSONParser.toString(t.getNegatives()));
        v.put("minResponseTimeNegativeMs",t.getMinResponseTimeNegativeMs());
        v.put("maxResponseTimeNegativeMs",t.getMaxResponseTimeNegativeMs());
        v.put("avgResponseTimeNegativeMs",t.getAvgResponseTimeNegativeMs());
        v.put("misses",JSONParser.toString(t.getMisses()));
        v.put("minResponseTimeMissMs",t.getMinResponseTimeMissMs());
        v.put("maxResponseTimeMissMs",t.getMaxResponseTimeMissMs());
        v.put("avgResponseTimeMissMs",t.getAvgResponseTimeMissMs());
        db.beginTransaction();
        db.insert(ImpairmentTest.class.getSimpleName(),null,v);
        db.endTransaction();
    }
}

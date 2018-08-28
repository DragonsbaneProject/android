package io.dragonsbane.neurocog.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.dragonsbane.data.ImpairmentTest;
import io.onemfive.core.infovault.BaseDAO;
import io.onemfive.core.infovault.InfoVaultDB;
import io.onemfive.data.DID;
import io.onemfive.data.util.JSONParser;

public class LoadImpairmentTestsDAO extends BaseDAO {

    private DID tester;
    private SQLiteDatabase db;
    private List<ImpairmentTest> tests;

    public LoadImpairmentTestsDAO(InfoVaultDB infoVaultDB, DID tester) {
        super(infoVaultDB);
        this.tester = tester;
        db = ((SQLiteInfoVaultDB) infoVaultDB).getDatabase();
    }

    @Override
    public void execute() throws Exception {
        tests = new ArrayList<>();
        Cursor c = db.rawQuery("select * from ImpairmentTest where tester="+tester.getIdentityHash(),null);
        ImpairmentTest t;
        while(!c.isAfterLast()) {
            t = new ImpairmentTest(tester,c.getString(c.getColumnIndex("name")));
            t.setBaseline(c.getInt(c.getColumnIndex("baseline")) == 1);
            t.setBloodAlcoholContent(c.getDouble(c.getColumnIndex("bloodAlcoholContent")));
            t.getCardsUsed().addAll((List<Integer>) JSONParser.parse(c.getString(c.getColumnIndex("cardsUsed"))));
            t.setTimeStarted(c.getLong(c.getColumnIndex("timeStarted")));
            t.setTimeEnded(c.getLong(c.getColumnIndex("timeEnded")));
            t.getSuccesses().addAll((List<Long>) JSONParser.parse(c.getString(c.getColumnIndex("successes"))));
            t.getInappropriates().addAll((List<Long>) JSONParser.parse(c.getString(c.getColumnIndex("inappropriates"))));
            t.getNegatives().addAll((List<Long>) JSONParser.parse(c.getString(c.getColumnIndex("negatives"))));
            t.getMisses().addAll((List<Long>) JSONParser.parse(c.getString(c.getColumnIndex("misses"))));
            tests.add(t);
            Log.i(LoadImpairmentTestsDAO.class.getSimpleName(),JSONParser.toString(t.toMap()));
        }
    }

    public List<ImpairmentTest> getTests() {
        return tests;
    }
}

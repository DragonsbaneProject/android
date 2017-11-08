package org.dragonsbaneproject.android.burstiq;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import org.dragonsbaneproject.android.burstiq.burstchain.BurstChainAPI;
import org.dragonsbaneproject.android.burstiq.dictionary.Dictionary;
import org.dragonsbaneproject.android.burstiq.dictionary.DictionaryAPI;
import org.dragonsbaneproject.android.util.HTTPHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * BurstIQ API Access
 */

public class BurstIQAPIService extends IntentService {

    private static final String DOMAMIN_URI = "https://stageaip.burstiq.com/";

    // Actions
    public static final String DICTIONARY_GET = "dragonsbane.burstiq.DICTIONARY_GET";
    public static final String DICTIONARY_FOUND = "dragonsbane.burstiq.DICTIONARY_FOUND";
    public static final String DICTIONARY_GET_ALL = "dragonsbane.burstiq.DICTIONARY_GET_ALL";

    // Data
    public static final String COLLECTION = "dragonsbane.burstiq.COLLECTION";
    public static final String DICTIONARY = "dragonsbane.burstiq.DICTIONARY";

    private Retrofit retrofit;

    private DictionaryAPI dictionaryAPI;
    private BurstChainAPI burstChainAPI;

    public BurstIQAPIService() {
        super(BurstIQAPIService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        switch(intent.getAction()){
            case DICTIONARY_GET_ALL: {
                getAllDictionaryEntries();
                break;
            }
            case DICTIONARY_GET: {
                getDictionary(intent.getStringExtra(COLLECTION));
                break;
            }
            default: {
                Log.w(BurstIQAPIService.class.getName(), BurstIQAPIService.class.getName()+" doesn't support action: "+intent.getAction());
            }
        }
    }

    private void getAllDictionaryEntries() {
        String jsonString = HTTPHelper.GET(DOMAMIN_URI+"/rest/metadata/dictionary/getAll", 5000, 3, this);
        try {
            JSONObject json = new JSONObject(jsonString);
            JSONArray dictionaries = json.getJSONArray("dictionaries");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getDictionary(String collection) {
        Call<Dictionary> call = dictionaryAPI.get(collection);
        Response<Dictionary> response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Dictionary dictionary = response.body();
        Intent out = new Intent(DICTIONARY_FOUND);
        out.putExtra(DICTIONARY, dictionary);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        retrofit = new Retrofit.Builder()
                .baseUrl(DOMAMIN_URI)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        dictionaryAPI = retrofit.create(DictionaryAPI.class);
        burstChainAPI = retrofit.create(BurstChainAPI.class);
    }
}

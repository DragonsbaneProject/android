package org.dragonsbaneproject.android.burstiq.dictionary;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * BurstIQ Dictionary API Interface
 */

public interface DictionaryAPI {

    @DELETE("rest/metadata/dictionary/delete")
    Call<Dictionary> delete(@Body Dictionary dictionary);

    @GET("rest/metadata/dictionary/get")
    Call<Dictionary> get(@Query("collection") String collection);

    @GET("rest/metadata/dictionary/getAll")
    Call<List<Dictionary>> getAll();

    @POST("rest/metadata/dictionary/save")
    Call<Dictionary> save(@Body Dictionary dictionary);

    @POST("rest/metadata/dictionary/saveOne")
    Call<Dictionary> saveOne(@Body Dictionary dictionary);
}

package org.dragonsbaneproject.android.burstiq.burstchain;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * BurstIQ Burst Chain API Interface
 */

public interface BurstChainAPI {

    @POST("burstchain/add/asset")
    Call<Asset> addAsset(@Body Asset asset);

    @Multipart
    @POST("burstchain/add/csvfile")
    Call<CsvFile> addCsv(
            @Part("description") String description,
            @Part("dataFile") RequestBody file,
            @Query("collectionName") String collectionName,
            @Query("assetMetadata") String assetMetadata,
            @Part("mappingFile") RequestBody mappingFile,
            @Query("emailResults") String emailResults);

    @Multipart
    @POST("burstchain/add/file")
    Call<File> addFile(
            @Part("description") String description,
            @Part("dataFile") RequestBody file,
            @Query("request") String request,
            @Query("emailResults") String emailResults);

    @POST("burstchain/add/transferAsset")
    Call<TransferAsset> addTransferAsset(@Body TransferAsset transferAsset);

    @POST("burstchain/add/updateAsset")
    Call<UpdateAsset> addUpdateAsset(@Body UpdateAsset updateAsset);



}

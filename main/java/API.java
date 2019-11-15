package java;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.*;

import java.util.Map;


/**
 * Static API class to be used with the API calls
 * Usage: API api = retrofit.create(API.class);
 */
public interface API {

    String baseURL = "https://platform.adobe.io/data/";
    String importURL = "foundation/import/batches/";
    String connectorURL = "foundation/connectors/connections/";
    String dataInletURL = "core/edge/inlet/";
    String datasetURL = "foundation/catalog/dataSets";
    String exportURL = "foundation/export/files/";
    String checkStatusURL = "foundation/catalog/batch/";

    //Create a Batch for Upload
    @POST("foundation/import/batches/")
    Call<Void> createBatch(@HeaderMap Map<String, String> headers);

    //Upload a batch that has been created (single batch file)
    @PUT("foundation/import/batches/{BATCH_ID}/datasets/{DATASET_ID}/files/{FILE_NAME}.parquet")
    Call<Void> uploadBatch(@HeaderMap Map<String, String> headers,
                           @Path("BATCH_ID") String batchId,
                           @Path("DATASET_ID") String datasetId,
                           @Path("FILE_NAME") String fileName);

    //Start the upload of a large file
    @POST("foundation/import/batches/{BATCH_ID}/datasets/{DATASET_ID}/files/{FILE_NAME}")
    Call<Void> startUploadingLargeFile(@HeaderMap Map<String, String> headers,
                                       @Path("BATCH_ID") String batchId,
                                       @Path("DATASET_ID") String datasetId,
                                       @Path("FILE_NAME") String fileName,
                                       @Query("action") String action);


    //Continue the upload of a large file
    @POST("foundation/import/batches/{BATCH_ID}/datasets/{DATASET_ID}/files/{FILE_NAME}")
    Call<Void> continueLargeFileUpload(@HeaderMap Map<String, String> headers,
                                       @Path("BATCH_ID") String batchId,
                                       @Path("DATASET_ID") String datasetId,
                                       @Path("FILE_NAME") String fileName);

    //Signal completion of a batch
    @POST("foundation/import/batches/{BATCH_ID}")
    Call<Void> signalBatchCompletion(@HeaderMap Map<String, String> headers,
                                     @Path("BATCH_ID") String batchId,
                                     @Query("action") String action);

    //Check Batch Status
    @GET("foundation/catalog/batch/{BATCH_ID}")
    Call<Void> checkBatchStatus(@HeaderMap Map<String, String> headers, @Path("BATCH_ID") String batchId);

    //Get DataSets (with filtering)
    @GET("foundation/catalog/dataSets?limit=5&properties=name,description,files")
    Call<Void> getFilteredDatasets(@HeaderMap Map<String, String> headers,
                                   @Query("limit") int limit,
                                   @Query("properties") String[] properties);

    //Get a specific Dataset object by ID
    @GET("foundation/catalog/dataSets{id}")
    Call<Void> getDatasetById(@HeaderMap Map<String, String> headers,
                              @Path("id") String id,
                              @Query("properties") String[] properties);

    /**
     * Insert API Calls here in the following format
     *
     * @CallType("URI") Call<Response Format> methodName(@Body NonGsonFormattedBodyType variableName)
     * <p>
     * See https://square.github.io/retrofit/ for documentation
     */

    Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    OkHttpClient okHttpClient = new OkHttpClient();

    Retrofit retrofitAuth = new Retrofit.Builder()
            .baseUrl(baseURL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();
}
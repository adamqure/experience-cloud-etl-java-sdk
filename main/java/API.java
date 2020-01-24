
import Models.CreateBatchBody;
import ParameterClasses.Classes.AuthToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.*;

import ParameterClasses.Abstracts.AuthInfoInterface;

import java.io.FileInputStream;
import java.util.Map;


/**
 * Abstract API class to be used with the API calls
 */
public abstract class API {

//    static final String BASE_URL = "https://platform.adobe.io/data/";
    static final String AUTH_URL = "https://ims-na1.adobelogin.com/ims/";
    static final String BATCH_INGESTION_URL = "https://platform.adobe.io/data/foundation/import/";
    static final String SCHEMA_REGISTRY_URL = "https://platform.adobe.io/data/foundation/schemaregistry/";
    static final String CATALOG_URL = "https://platform.adobe.io/data/foundation/catalog/";

    interface SchemaService {
//        Create a Schema
//    POST /tenant/schemas
//    curl -X POST \
//    https://platform.adobe.io/data/foundation/schemaregistry/tenant/schemas \
//            -H 'Authorization: Bearer {ACCESS_TOKEN}' \
//            -H 'x-api-key: {API_KEY}' \
//            -H 'x-gw-ims-org-id: {IMS_ORG}' \
//            -H 'x-sandbox-name: {SANDBOX_NAME}' \
//            -H 'Content-Type: application/json' \
//            -d '{
//            "type": "object",
//            "title": "Loyalty Members",
//            "description": "Information for all members of the loyalty program",
//            "allOf": [
//    {
//        "$ref": "https://ns.adobe.com/xdm/context/profile"
//    }
//  ]
//}
        @POST("tenant/schemas")
        Call<Void> createSchema(@HeaderMap Map<String, String> headers, @Body String body);

        //Lookup a schema
//        curl -X GET \
//        https://platform.adobe.io/data/foundation/schemaregistry/tenant/schemas/https%3A%2F%2Fns.adobe.com%2F{TENANT_ID}%2Fschemas%2F533ca5da28087c44344810891b0f03d9\
//                -H 'Authorization: Bearer {ACCESS_TOKEN}' \
//                -H 'x-api-key: {API_KEY}' \
//                -H 'x-gw-ims-org-id: {IMS_ORG}' \
//                -H 'x-sandbox-name: {SANDBOX_NAME}' \
//                -H 'Accept: application/vnd.adobe.xed+json; version=1'

        @GET("tenant/schemas/{SCHEMA_ID}")
        Call<Void> lookupSchema(@HeaderMap Map<String, String> headers, @Query("SCHEMA_ID") String schemaId);

        //Get Tenant ID
//        curl -X GET \
//        https://platform.adobe.io/data/foundation/schemaregistry/stats \
//                -H 'Authorization: Bearer {ACCESS_TOKEN}' \
//                -H 'x-api-key: {API_KEY}' \
//                -H 'x-gw-ims-org-id: {IMS_ORG}' \
//                -H 'x-sandbox-name: {SANDBOX_NAME}'
        @GET("stats")
        Call<Void> lookupTenantID(@HeaderMap Map<String, String> headers);
    }

    interface IngestionService {
        //Create a Batch
//        curl -X POST "https://platform.adobe.io/data/foundation/import/batches" \
//                -H "Content-Type: application/json" \
//                -H "x-gw-ims-org-id: {IMS_ORG}" \
//                -H "x-sandbox-name: {SANDBOX_NAME}" \
//                -H "Authorization: Bearer {ACCESS_TOKEN}" \
//                -H "x-api-key : {API_KEY}"
//                -d '{
//                "datasetId": "{DATASET_ID}",
//                "inputFormat": {
//                      "format": "json"
//                                }
//                      }'

        @POST("batches")
        Call<CreateBatchBody> createBatch(@HeaderMap Map<String, String> headers, @Body CreateBatchBody body);


        //Upload Files to Batch
//        curl -X PUT 'https://platform.adobe.io/data/foundation/import/batches/5d01230fc78a4e4f8c0c6b387b4b8d1c/datasets/5c8c3c555033b814b69f947f/files/loyaltyData.parquet' \
//                -H 'content-type: application/octet-stream' \
//                -H 'x-api-key : {API_KEY}' \
//                -H 'x-gw-ims-org-id: {IMG_ORG}' \
//                -H 'Authorization: Bearer {ACCESS_TOKEN}' \
//                --data-binary '@{FILE_PATH_AND_NAME}.json'


        //TODO: The body is an octet stream in an object
        @Multipart
        @PUT("batches/{BATCH_ID}/datasets/{DATASET_ID}/files/{FILE_NAME}")
        Call<Void> uploadFileToBatch(@HeaderMap Map<String, String> headers,
                                     @Query("BATCH_ID") String batchId,
                                     @Query("DATASET_ID") String datasetId,
                                     @Query("FILE_NAME") String fileName,
                                     @Body FileInputStream body);

        //Signal Batch Complete
//        curl -X POST "https://platform.adobe.io/data/foundation/import/batches/5d01230fc78a4e4f8c0c6b387b4b8d1c?action=COMPLETE" \
//                -H 'x-api-key : {API_KEY}' \
//                -H 'x-gw-ims-org-id: {IMG_ORG}' \
//                -H 'Authorization: Bearer {ACCESS_TOKEN}'
        @POST("batches/{BATCH_ID}?action=COMPLETE")
        Call<Void> signalBatchComplete(@HeaderMap Map<String, String> headers,
                                       @Query("BATCH_ID") String batchId);

        //Cancel a Batch
//        curl -X POST "https://platform.adobe.io/data/foundation/import/batches/{BATCH_ID}?action=ABORT" \
//                -H "x-gw-ims-org-id: {IMS_ORG}" \
//                -H "x-sandbox-name: {SANDBOX_NAME}" \
//                -H "Authorization: Bearer {ACCESS_TOKEN}" \
//                -H "x-api-key : {API_KEY}"
        @POST("batches/{BATCH_ID}?action=ABORT")
        Call<Void> cancelBatch(@HeaderMap Map<String, String> headers, @Query("BATCH_ID") String batchId);
    }

    interface CatalogService {
        //Create a Dataset
//        curl -X POST \
//                'https://platform.adobe.io/data/foundation/catalog/dataSets?requestDataSource=true' \
//                -H 'Authorization: Bearer {ACCESS_TOKEN}' \
//                -H 'Content-Type: application/json' \
//                -H 'x-api-key: {API_KEY}' \
//                -H 'x-gw-ims-org-id: {IMS_ORG}' \
//                -H 'x-sandbox-name: {SANDBOX_NAME}' \
//                -d '{
//                "name":"LoyaltyMembersDataset",
//                "schemaRef": {
//            "id": "https://ns.adobe.com/{TENANT_ID}/schemas/719c4e19184402c27595e65b931a142b",
//                    "contentType": "application/vnd.adobe.xed+json;version=1"
//        },
//                "fileDescription": {
//            "persisted": true,
//                    "containerFormat": "parquet",
//                    "format": "parquet"
//        }
//    }'
        @POST("dataSets")
        Call<Void> createDataset(@HeaderMap Map<String, String> headers, @Body String body, @Query("requestDataSource") boolean requestDataSource);


        //Get upload status
//        curl -X GET \
//                'https://platform.adobe.io/data/foundation/catalog/batches?batch=5d01230fc78a4e4f8c0c6b387b4b8d1c' \
//                -H 'x-api-key : {API_KEY}' \
//                -H 'x-gw-ims-org-id: {IMG_ORG}' \
//                -H 'x-sandbox-name: {SANDBOX_NAME}' \
//                -H 'Authorization: Bearer {ACCESS_TOKEN}'
        @GET("batches?batch={BATCH_ID}")
        Call<Void> getUploadStatus(@HeaderMap Map<String, String> headers,
                                   @Query("BATCH_ID") String batchId);

        //Get All Batches
//        curl -X GET 'https://platform.adobe.io/data/foundation/catalog/batches/' \
//                -H 'Authorization: Bearer {ACCESS_TOKEN}' \
//                -H 'x-api-key: {API_KEY}' \
//                -H 'x-gw-ims-org-id: {IMS_ORG}' \
//                -H 'x-sandbox-name: {SANDBOX_NAME}'
        @GET("batches")
        Call<Void> getBatchesList(@HeaderMap Map<String, String> headers);

//        Get a specific Dataset object by ID
        @GET("foundation/catalog/dataSets{id}")
        Call<Void> getDatasetById(@HeaderMap Map<String, String> headers,
                                  @Path("id") String id,
                                  @Query("properties") String[] properties);

    }

    interface AuthService {
        //Exchange JWT for Auth Token
        @FormUrlEncoded
        @POST("exchange/jwt/")
        Call<AuthToken> getAuthToken(@Field("client_id") String id, @Field("client_secret") String secret, @Field("jwt_token") String jwt);
    }

    public static SchemaService getSchemaService() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        OkHttpClient okHttpClient = new OkHttpClient();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SCHEMA_REGISTRY_URL)
                .addConverterFactory(GsonConverterFactory.create()) //assuming JSON
                .build();
        return retrofit.create(SchemaService.class);
    }

    public static IngestionService getIngestionService() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        OkHttpClient okHttpClient = new OkHttpClient();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BATCH_INGESTION_URL)
                .addConverterFactory(GsonConverterFactory.create()) //assuming JSON
                .build();
        return retrofit.create(IngestionService.class);
    }

    public static CatalogService getCatalogService() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        OkHttpClient okHttpClient = new OkHttpClient();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(CATALOG_URL)
                .addConverterFactory(GsonConverterFactory.create()) //assuming JSON
                .build();
        return retrofit.create(CatalogService.class);
    }

    public static AuthService getAuthService() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        OkHttpClient okHttpClient = new OkHttpClient();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AUTH_URL)
                .addConverterFactory(GsonConverterFactory.create()) //assuming JSON
                .build();
        return retrofit.create(AuthService.class);
    }

//    Gson gson = new GsonBuilder()
//            .setLenient()
//            .create();
//
//    OkHttpClient okHttpClient = new OkHttpClient();
//
//    Retrofit retrofitAuth = new Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .client(okHttpClient)
//            .addConverterFactory(GsonConverterFactory.create(gson))
//            .build();


}

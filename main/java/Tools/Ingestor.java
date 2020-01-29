package Tools;

import API.API;
import API.API.IngestionService;
import Models.CreateBatchBody;
import ParameterClasses.AuthInfo;
import ToolsInterfaces.IngestorInterface;
import ParameterClasses.Schema;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Basic Ingestor that implements the IngestorInterface
 */
public class Ingestor implements IngestorInterface {
    IngestionService ingestionService = API.getIngestionService();

    @Override
    public String createBatch(AuthInfo authInfo, String datasetId){
        System.out.println("CREATING BATCH");
        Map<String, String> headers = generateHeaders(authInfo, "application/json");

        CreateBatchBody createBatchData = new CreateBatchBody(datasetId);
        Call<JsonElement> call = ingestionService.createBatch(headers, createBatchData);
        Response<JsonElement> response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (response != null) {
            JsonObject respBody = response.body().getAsJsonObject();
            String batchId = respBody.get("id").toString();
            System.out.println("BATCH ID: " + batchId);

            batchId = batchId.replace("\"", "");
            return batchId;
        }
        return null;
    }

    @Override
    public boolean uploadFileToBatch(AuthInfo authInfo, Schema schema, String batchId, String datasetId, String filename) {
        System.out.println("UPLOADING FILE: " + filename);
        Map<String, String> headers = generateHeaders(authInfo, "application/octet-stream");
//        System.out.println("HEADERS: " + headers.toString());

        try {
            byte[] fileContents = readFile(filename);
            Call<Void> call = API.getIngestionService().uploadFileToBatch(headers, batchId, datasetId, filename, fileContents);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    System.out.println("FILE UPLOADED: " + response.toString());

                    Map<String, String> headers = generateHeaders(authInfo, null);
                    Call<Void> finishBatchCall = ingestionService.signalBatchComplete(headers, batchId);
                    try {
                        System.out.println("FINISHING BATCH");
                        Response<Void> finishBatchResponse = finishBatchCall.execute();
                        System.out.println("BATCH FINISHED: " + finishBatchResponse.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    System.out.println("Failure. Call:\n" + call.toString() + ",\nThrowable:\n" + t.toString());
                }
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean signalBatchComplete(AuthInfo authInfo, String batchId){
        return false;
    }

    @Override
    public boolean cancelBatch(AuthInfo authInfo, String batchId){
        return false;
    }

    private Map<String, String> generateHeaders(AuthInfo authInfo, String contentType) {
        Map<String, String> headers = new HashMap<>();
        if (contentType != null) {
            headers.put("Content-Type", contentType);
        }
        headers.put("x-gw-ims-org-id", authInfo.getImsOrgId());
        headers.put("Authorization", "Bearer " + authInfo.getAccessToken());
        headers.put("x-api-key", authInfo.getApiKey());
        return headers;
    }

    private byte[] readFile(String filePath) {
        byte[] content = null;
        try {
            content = Files.readAllBytes( Paths.get(filePath) );
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }
}

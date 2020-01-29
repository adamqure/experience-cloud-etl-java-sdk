package Tools;

import API.API;
import ParameterClasses.AuthInfo;
import ToolsInterfaces.CataloguerInterface;
import com.google.gson.JsonElement;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Basic Cataloguer that implements the Cataloguer Interface
 */
public class Cataloguer implements CataloguerInterface
{
    @Override
    public void createDataset(){

    }
    @Override
    public void getUploadStatus(AuthInfo authInfo, String batchId){
        System.out.println("GETTING BATCH STATUS");
        Map<String, String> headers = generateHeaders(authInfo, null);

        Call<JsonElement> call = API.getCatalogService().getUploadStatus(headers, batchId);
//        System.out.println(headers);
        Response<JsonElement> response;
        try {
            response = call.execute();
            System.out.println("STATUS RETRIEVED: " + response.toString());
            System.out.println("BATCH STATUS: " + response.body());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void generateReport(){

    }
    @Override
    public void getBatchesList(){

    }
    @Override
    public void getDatasetByID(){

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
}

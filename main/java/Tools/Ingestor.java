package Tools;

import API.API;
import API.API.IngestionService;
import Exceptions.InvalidCallException;
import Exceptions.ParameterException;
import Models.CreateBatchBody;
import ParameterClasses.AuthInfo;
import ToolsInterfaces.IngestorInterface;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static com.google.gson.stream.JsonToken.*;

/**
 * Basic Ingestor that implements the IngestorInterface
 */
public class Ingestor implements IngestorInterface {
    IngestionService ingestionService = API.getIngestionService();
    final long MAX_FILE_SIZE = (long) 1e8;
    boolean isWaiting = false;

    /**
     * Creates new batch for uploading files
     * @param authInfo contains auth header information for request
     * @param datasetId is the id of the dataset being uploaded to
     * @return is the id of the newly-created batch
     */
    @Override
    public String createBatch(AuthInfo authInfo, String datasetId) throws IOException, ParameterException, InvalidCallException
    {
        checkAuthInfo(authInfo);
        if(datasetId == null || datasetId == "")
        {
            throw new ParameterException("datasetID cannot be null or empty");
        }
        System.out.println("CREATING BATCH");
        Map<String, String> headers = generateHeaders(authInfo, "application/json");

        CreateBatchBody createBatchData = new CreateBatchBody(datasetId);
        Call<JsonElement> call = ingestionService.createBatch(headers, createBatchData);

        Response<JsonElement> response = call.execute();
        if (response.isSuccessful() && response.body() != null)
        {
            JsonObject respBody = response.body().getAsJsonObject();
            String batchId = respBody.get("id").toString();
            System.out.println("BATCH ID: " + batchId);

            batchId = batchId.replace("\"", "");
            return batchId;
        }
        else
        {
            String message = response.errorBody().string();
            throw new InvalidCallException("Error: " + response.message()+"\ncode: " +response.code()+"\n message: "+message);
        }
    }

    /**
     * Adds a file synchronously to an existing batch for upload
     * @param authInfo contains auth header information for request
     * @param batchId is the id of the batch the file is to be added to
     * @param datasetId is the id of the dataset the file is to be uploaded to
     * @param filename is the name of the file to be uploaded
     * @return true if file is added successfully, otherwise false
     */
    @Override
    public boolean addFileToBatchSync(AuthInfo authInfo, String batchId, String datasetId, String filename) {
        return addFileToBatch(authInfo, batchId, datasetId, filename, true);
    }

    /**
     * Adds a file asynchronously to an existing batch for upload
     * @param authInfo contains auth header information for request
     * @param batchId is the id of the batch the file is to be added to
     * @param datasetId is the id of the dataset the file is to be uploaded to
     * @param filename is the name of the file to be uploaded
     * @return true if file is added successfully, otherwise false
     */
    @Override
    public boolean addFileToBatch(AuthInfo authInfo, String batchId, String datasetId, String filename) {
        return addFileToBatch(authInfo, batchId, datasetId, filename, false);
    }

    private boolean addFileToBatch(AuthInfo authInfo, String batchId, String datasetId, String filename, boolean runSync) throws ParameterException, InvalidCallException {
        checkAuthInfo(authInfo);
        if(batchId == null || batchId == "")
        {
            throw new ParameterException("Batch ID cannot be null or empty");
        }
        if(filename == null || filename == "")
        {
            throw new ParameterException("Filename cannot be null or empty");
        }
        if(datasetId == null || datasetId == "")
        {
            throw new ParameterException("DataSet ID cannot be null or empty");
        }
        System.out.println("UPLOADING FILE: " + filename);
        try
        {
            long fileSize = getFileSize(filename);
            System.out.println("FILE SIZE: " + fileSize);

            if (fileSize > MAX_FILE_SIZE) {
                System.out.println("LARGE FILE DETECTED");

                boolean success = true;
                List<String> splitFiles = splitLargeFile(filename);
                for (String splitFile : splitFiles)
                {
                    splitFile = "temp/" + splitFile;
                    success = addSmallFile(authInfo, batchId, datasetId, splitFile, runSync) && success;
                }
                return success;
            }
            else
                {
                System.out.println("SMALL FILE DETECTED");
                return addSmallFile(authInfo, batchId, datasetId, filename, runSync);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Splits a large json file into smaller json files that follow the same format as the original
     * @param filename is the name of the file to be split
     * @return is a List of the names of the generated files
     */
    public List<String> splitLargeFile(String filename) throws Exception {
        print("SPLITTING LARGE FILE");
        File tempDir = new File("temp");
        if (tempDir.exists())
        {
            deleteDir(tempDir);
        }
        while (tempDir.exists())
        {
            Thread.sleep(1000);
        }
        tempDir.mkdir();

        File inFile = new File(filename);
        FileReader fileReader = new FileReader(inFile);
        JsonReader jsonReader = new JsonReader(fileReader);
        jsonReader.setLenient(true);

        jsonReader.beginArray();

        int numFiles = 0;
        boolean endOfFile = false;
        while(!endOfFile) {
            String newFilename = "temp/file" + (numFiles + 1) + ".json";
            File newFile = new File(newFilename);
            newFile.createNewFile();
            print("CREATING TEMP FILE: " + newFilename);

            FileOutputStream oStream = new FileOutputStream(newFile);
            write("[", oStream);

            endOfFile = !streamObject(jsonReader, oStream);
            long fileSize = newFile.length();
            long objSize = fileSize;

            boolean isNextObj = jsonReader.peek().equals(BEGIN_OBJECT);
            if (isNextObj)
            {
                write(",", oStream);
            }
            else {
                break;
            }

            final int FILE_CHUNK_SIZE = 10000;
            StringBuilder chunk = new StringBuilder();
            boolean fileHasSpace = fileSize + objSize * FILE_CHUNK_SIZE < MAX_FILE_SIZE;
            while (fileHasSpace) {
                for (int i = 0; i < FILE_CHUNK_SIZE; i++)
                    streamObject(jsonReader, chunk);
                write(chunk.toString(), oStream);
                chunk.setLength(0);

                fileSize = newFile.length();
                fileHasSpace = fileSize + (objSize * FILE_CHUNK_SIZE) < MAX_FILE_SIZE;
                isNextObj = jsonReader.peek().equals(BEGIN_OBJECT);
                if (isNextObj && fileHasSpace)
                {
                    write(",", oStream);
                }
                else if (!isNextObj)
                {
                    endOfFile = true;
                    break;
                }
            }
            write("]", oStream);
            numFiles++;
        }

        jsonReader.endArray();
        jsonReader.close();

        String[] filesArr = tempDir.list();
        if (filesArr != null) {
            return Arrays.asList(filesArr);
        }
        return null;
    }

    private boolean streamObject(JsonReader reader, StringBuilder builder) throws Exception {
        JsonElement element = null;
        try {
            element = new Gson().fromJson(reader, JsonElement.class);
        }
        catch (IllegalArgumentException e) {
            return false;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if (element != null) {
            builder.append(element.toString());
            return true;
        }
        return false;
    }

    private boolean streamObject(JsonReader reader, FileOutputStream oStream) throws Exception {
        JsonElement element = null;
        try {
            element = new Gson().fromJson(reader, JsonElement.class);
            if (element == null) {
                return false;
            }
        }
        catch (IllegalArgumentException e) {
            return false;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if (element != null) {
            write(element.toString(), oStream);
        }
        return true;
    }

    private void write(String output, FileOutputStream stream) throws Exception {
        stream.write(bytes(output));
    }

    private byte[] bytes(String myString) {
        return myString.getBytes();
    }

    private void print(String output) {
        System.out.println(output);
    }

    private boolean addSmallFile(AuthInfo authInfo, String batchId, String datasetId, String filename, boolean runSync) throws IOException {
        Map<String, String> headers = generateHeaders(authInfo, "application/octet-stream");
        System.out.println("HEADERS: " + headers.toString());
        byte[] fileContents = readFile(filename);

        setIsWaiting(runSync);
        Call<Void> call = API.getIngestionService().uploadFileToBatch(headers, batchId, datasetId, filename, fileContents);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                System.out.println("FILE UPLOADED: " + response.toString());
                setIsWaiting(false);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println("Failure. Call:\n" + call.toString() + ",\nThrowable:\n" + t.toString());
                setIsWaiting(false);
            }
        });
        awaitResponse();
        return true;
    }

    /**
     * Signals that a batch is complete and no more files will be added
     * @param authInfo contains the auth header info for the request
     * @param batchId is the id of the batch being completed
     */
    @Override
    public void signalBatchComplete(AuthInfo authInfo, String batchId) throws IOException, ParameterException, InvalidCallException
    {
        if(batchId == null || batchId == "")
        {
            throw new ParameterException("batchID cannot be null or empty");
        }
        if(authInfo.getAccessToken() == null || authInfo.getAccessToken() == "")
        {
            throw new ParameterException("access Token cannot be null or an empty string");
        }
        if(authInfo.getApiKey() == null || authInfo.getApiKey() == "")
        {
            throw new ParameterException("API key cannot be null or empty");
        }
        if(authInfo.getImsOrgId() == null || authInfo.getImsOrgId() == "")
        {
            throw new ParameterException("IMS Org ID cannot be null or empty");
        }
        Map<String, String> headers = generateHeaders(authInfo, null);
        Call<Void> finishBatchCall = ingestionService.signalBatchComplete(headers, batchId);
        System.out.println("FINISHING BATCH");
        Response<Void> finishBatchResponse = finishBatchCall.execute();
        if(!finishBatchResponse.isSuccessful())
        {
            String message = finishBatchResponse.errorBody().string();
            System.out.println(message);
            throw new InvalidCallException(finishBatchResponse.message() + "\nCode" + finishBatchResponse.code() + "\n" + message);
        }
        System.out.println("BATCH FINISHED: " + finishBatchResponse.toString());
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

    private long getFileSize(String filename) {
        return new File(filename).length();
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

    private void awaitResponse() {
        while (getIsWaiting()) {
            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void checkAuthInfo(AuthInfo authInfo) throws ParameterException {
        if (authInfo == null) {
            throw new ParameterException("AuthInfo object cannot be null when attempting API operations");
        }
        if (authInfo.getApiKey() == null || authInfo.getApiKey() == "") {
            throw new ParameterException("API Key cannot be null or empty");
        }
        if (authInfo.getImsOrgId() == null || authInfo.getImsOrgId() == "") {
            throw new ParameterException("IMS Org ID cannot be null or empty");
        }
        if (authInfo.getAccessToken() == null || authInfo.getAccessToken() == "") {
            throw new ParameterException("Access Token cannot be null or empty");
        }
    }

    private boolean getIsWaiting() {
        return this.isWaiting;
    }

    private void setIsWaiting(boolean isWaiting) {
        this.isWaiting = isWaiting;
    }

    private void deleteDir(File dir) {
        String[] entries = dir.list();
        if (entries == null) return;

        for(String s: entries){
            File currentFile = new File(dir.getPath(),s);
            currentFile.delete();
        }
        dir.delete();
    }
}

package Tools;

import API.API;
import ParameterClasses.*;
import ToolsInterfaces.*;

import com.google.gson.Gson;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.*;

import io.jsonwebtoken.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Basic importer class that implements the ImporterInterface
 * Importer uses an ingestor, validator, and cataloguer internally
 */
public class Importer implements ImporterInterface {
    private IngestorInterface ingestor;
    private CataloguerInterface cataloguer;
    private ValidatorInterface schemaValidator;

    private AuthInfo authInfo;

    public Importer() {
        Gson deserializer = new Gson();
        File config = new File("config.json");
        authInfo = null;
        try {
            String text = new Scanner(config).useDelimiter("\\A").next();
            authInfo = deserializer.fromJson(text, AuthInfo.class);
            authInfo.addAuthToken(new AuthToken());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        createJwt();

        cataloguer = new Cataloguer();
        ingestor = new Ingestor();
        schemaValidator = new Validator();
    }

    void createJwt() {
        //Set a time for the JWT to expire, 10 minutes from the current time
        Date exp = new Date();
        exp.setTime(exp.getTime() + 600000);
        byte[] bytes;
        RSAPrivateKey privKey = null;
        try {
            String keyString = authInfo.getRsaKey();
            keyString = keyString.replace("\\s+", "");
            keyString = keyString.replace("\n", "");
            keyString = keyString.replace("-----BEGIN PRIVATE KEY-----", "");
            keyString = keyString.replace("-----END PRIVATE KEY-----", "");
            KeyFactory factory = KeyFactory.getInstance("RSA");
            bytes = Base64.getDecoder().decode(keyString);
            KeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
            privKey = (RSAPrivateKey) factory.generatePrivate(keySpec);
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.out.println("Some error occurred when attempting to get the Key factory for the RSAPrivateKey");
        }
        catch (InvalidKeySpecException e) {
            e.printStackTrace();
            System.out.println("Invalid Key spec when attempting to create a JWT with your RSA private key. Please check your" +
                    "config file and RSA key");
        }
        if (privKey == null) {
            System.out.println("Something went wrong in the creation of the JSON Web Token (JWT) Most likely problem is" +
                    "a config file not formatted correctly or an incorrect RSA key formatting");
        }
        SignatureAlgorithm rs = SignatureAlgorithm.RS256;
        Map<String, Object> metas = new HashMap<>();
        metas.put("https://ims-na1.adobelogin.com/s/ent_dataservices_sdk", Boolean.TRUE);
        String Jwt = Jwts.builder()
                .setIssuer(authInfo.getImsOrgId())
                .setExpiration(exp)
                .setSubject(authInfo.getSubject())
                .setAudience("https://ims-na1.adobelogin.com/c/" + authInfo.getApiKey())
                .addClaims(metas)
                .signWith(privKey, rs)
                .compact();
        byte[] holder = Jwt.getBytes(StandardCharsets.ISO_8859_1);
        Jwt = new String(holder, StandardCharsets.UTF_8);
        authInfo.setJwt(Jwt);
        this.exchangeJwtAuth();
    }

    void exchangeJwtAuth() {
        Call<AuthToken> call = API.getAuthService().getAuthToken(authInfo.getApiKey(), authInfo.getClientSecret(), authInfo.getJwt());
        call.enqueue(new Callback<AuthToken>(){
            @Override
            // TODO: Complete error checking
            public void onResponse(Call<AuthToken> call, Response<AuthToken> response) {
                authInfo.addAuthToken(response.body());
                System.out.println("EXCHANGED JWT: " + authInfo.getAccessToken());
            }

            @Override
            public void onFailure(Call<AuthToken> call, Throwable t) {
                System.out.println("Error when exchanging JWT for Access Token");
            }
        });
        while (authInfo.getAccessToken().equals("")) {
            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // TODO: Run this asynchronously so that closeBatch can wait for addFileToBatch to finish
    /**
     * Asynchronously uploads a file to a given dataset.
     * It creates a new batch, adds the file to the batch, and signals the batch is complete.
     * @param filename is the name of the file to be uploaded.
     * @param datasetId is the id of the dataset to be uploaded to.
     * @return is the id of the batch created.
     */
    public String uploadFile(String filename, String datasetId) {
        String batchId = createBatch(datasetId);
        addFileToBatch(batchId, datasetId, filename);
        closeBatch(batchId);
        return batchId;
    }

    /**
     * Synchronously uploads a file to a given dataset.
     * It creates a new batch, adds the file to the batch, and signals the batch is complete.
     * @param filename is the name of the file to be uploaded.
     * @param datasetId is the id of the dataset to be uploaded to.
     * @return is the id of the batch created.
     */
    public String uploadFileSync(String filename, String datasetId) {
        String batchId = createBatch(datasetId);
        addFileToBatchSync(batchId, datasetId, filename);
        closeBatch(batchId);
        return batchId;
    }

    /**
     * Creates a batch for uploading files to the specified given dataset.
     * @param datasetId is the id of the dataset to be uploaded to.
     * @return is the id of the batch created.
     */
    public String createBatch(String datasetId) {
        try {
            return ingestor.createBatch(authInfo, datasetId);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Adds a file to an existing batch to be uploaded
     * @param batchId is the id of the batch the file is to be added to.
     * @param datasetId is the id of the dataset the file is to be uploaded to.
     * @param filename is the path of the file to be uploaded.
     */
    public void addFileToBatch(String batchId, String datasetId, String filename) {
        try {
            ingestor.addFileToBatch(authInfo, batchId, datasetId, filename);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a file synchronously to an existing batch to be uploaded
     * @param batchId is the id of the batch the file is to be added to.
     * @param datasetId is the id of the dataset the file is to be uploaded to.
     * @param filename is the path of the file to be uploaded.
     */
    public void addFileToBatchSync(String batchId, String datasetId, String filename) {
        try {
            ingestor.addFileToBatchSync(authInfo, batchId, datasetId, filename);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes an existing batch, signaling no more files will be added.
     * @param batchId is the id of the batch to be closed.
     */
    public void closeBatch(String batchId) {
        try {
            ingestor.signalBatchComplete(authInfo, batchId);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the status for a batch that has been created.
     * @param batchId is the id of the batch whose status is requested.
     * @return is the status of the batch.
     */
    public String getBatchStatus(String batchId) {
        try {
            return cataloguer.getBatchStatus(authInfo, batchId);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets the auth header information being used for requests
     * @return is an object containing the auth header info
     */
    public AuthInfo getAuthInfo() {
        return authInfo;
    }
}

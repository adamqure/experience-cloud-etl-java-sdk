package Tools;

import API.API;
import ParameterClasses.*;
import ToolsInterfaces.*;
import Tools.*;

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
 * Tools.Importer uses an ingestor, validator, and cataloguer
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

    private void createJwt() {
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
            System.out.println("Some error occured when attempting to get the Key factory for the RSAPrivateKey");
        }
        catch (InvalidKeySpecException e) {
            e.printStackTrace();
            System.out.println("Invalid Key spec when attempting to create a JWT with your RSA private key. Please check your" +
                    "config file and RSA key");
        }
        if (privKey == null) {
            //TODO Set proper error handling in the case that we get to here without a proper Key
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

    private void exchangeJwtAuth() {
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
    }

    public String uploadFile(String filename, Schema schema, String datasetId) {
        while (authInfo.getAccessToken().equals("")) {
            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        String batchId = ingestor.createBatch(authInfo, datasetId);
        if (batchId != null) {
            ingestor.uploadFileToBatch(authInfo, schema, batchId, datasetId, filename);
        }

        ingestor.signalBatchComplete(authInfo, batchId);
        return batchId;
    }

    public void getBatchStatus(String batchId) {
        while (authInfo.getAccessToken().equals("")) {
            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        cataloguer.getUploadStatus(authInfo, batchId);
    }
}

import Models.CreateBatchBody;
import ParameterClasses.Abstracts.DataSetIdInterface;
import ParameterClasses.Abstracts.SchemaInterface;
import ParameterClasses.Classes.AuthInfo;
import ParameterClasses.Classes.AuthToken;
import ParameterClasses.Classes.DataSetID;
import ToolsInterfaces.*;
import Tools.Cataloguer;
import Tools.Ingestor;
import Tools.Validator;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
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
 * Importer uses an ingestor, validator, and cataloguer
 */
public class Importer
{
    private IngestorInterface dataIngestor;
    private CataloguerInterface dataCataloguer;
    private ValidatorInterface schemaValidator;
    private AuthInfo authInfo;
    private Gson deserializer;

    public Importer()
    {
        deserializer = new Gson();
        File config = new File("config.json");
        authInfo = null;
        try
        {
            String text = new Scanner(config).useDelimiter("\\A").next();
            authInfo = deserializer.fromJson(text, AuthInfo.class);
            authInfo.addAuthToken(new AuthToken());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        createJwt();
        dataCataloguer = new Cataloguer();
        dataIngestor = new Ingestor();
        schemaValidator = new Validator();
    }

    public void createJwt()
    {
        //Set a time for the JWT to expire, 10 minutes from the current time
        Date exp = new Date();
        exp.setTime(exp.getTime() + 600000);
        byte[] bytes;
        RSAPrivateKey privKey = null;
        try
        {
            String keyString = authInfo.getRsaKey();
            keyString = keyString.replace("\\s+", "");
            keyString = keyString.replace("\n", "");
            keyString = keyString.replace("-----BEGIN PRIVATE KEY-----", "");
            keyString = keyString.replace("-----END PRIVATE KEY-----", "");
//            System.out.println(keyString);
            KeyFactory factory = KeyFactory.getInstance("RSA");
            bytes = Base64.getDecoder().decode(keyString);
            KeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
            privKey = (RSAPrivateKey) factory.generatePrivate(keySpec);
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
            System.out.println("Some error occured when attempting to get the Key factory for the RSAPrivateKey");
        }
        catch (InvalidKeySpecException e)
        {
            e.printStackTrace();
            System.out.println("Invalid Key spec when attempting to create a JWT with your RSA private key. Please check your" +
                    "config file and RSA key");
        }
        if (privKey == null)
        {
            //TODO Set proper error handling in the case that we get to here without a proper Key
            System.out.println("Something went wrong in the creation of the JSON Web Token (JWT) Most likely problem is" +
                    "a config file not formatted correctly or an incorrect RSA key formatting");
        }
        SignatureAlgorithm rs = SignatureAlgorithm.RS256;
        Map<String, Object> metas = new HashMap<String, Object>();
        metas.put("https://ims-na1.adobelogin.com/s/ent_dataservices_sdk", Boolean.TRUE);
        String Jwt = Jwts.builder()
                .setIssuer(authInfo.getImsOrgId())
                .setExpiration(exp)
                .setSubject(authInfo.getSubject())
                .setAudience("https://ims-na1.adobelogin.com/c/" + authInfo.getApiKey())
                .addClaims(metas)
                .signWith(privKey, rs)
                .compact();
//        System.out.println(Jwt);
        byte[] holder = Jwt.getBytes(StandardCharsets.ISO_8859_1);
        Jwt = new String(holder, StandardCharsets.UTF_8);
        authInfo.setJwt(Jwt);
        this.exchangeJwtAuth();
        this.Upload(null, null, new DataSetID("5e29e7e984479018a93e70a7"));
    }

    public void exchangeJwtAuth()
    {
        Call<AuthToken> call = API.getAuthService().getAuthToken(authInfo.getApiKey(), authInfo.getClientSecret(), authInfo.getJwt());
        call.enqueue(new Callback<AuthToken>(){
            @Override
            //TODO Complete error checking
            public void onResponse(Call<AuthToken> call, Response<AuthToken> response) {
                if(response != null) {
                    authInfo.addAuthToken(response.body());
                    System.out.println(authInfo.getAccessToken());
                }
            }

            @Override
            public void onFailure(Call<AuthToken> call, Throwable t) {
                System.out.println("Error when exchanging JWT for Access Token");
            }
        });
    }

    public void Upload(FileInputStream toUpload, SchemaInterface schema, DataSetIdInterface DSId)
    {
        while (authInfo.getAccessToken() == "")
        {
            try
            { Thread.sleep(1000); }
            catch (InterruptedException e)
            { Thread.currentThread().interrupt(); }
        }
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("x-gw-ims-org-id", authInfo.getImsOrgId());
        headers.put("Authorization", "Bearer " + authInfo.getAccessToken());
        headers.put("x-api-key", authInfo.getApiKey());

        CreateBatchBody createBatchData = new CreateBatchBody(DSId.getIdentifier());
        System.out.println("Create Batch Body:\n" + createBatchData.toString());
//        String serialized = deserializer.toJson(createBatchData);
//        System.out.println(serialized);

        Call<CreateBatchBody> call = API.getIngestionService().createBatch(headers, createBatchData);
        call.enqueue(new Callback<CreateBatchBody>() {
            @Override
            public void onResponse(Call<CreateBatchBody> call, Response<CreateBatchBody> response) {
                System.out.println("Success\n" + response.body());
            }

            @Override
            public void onFailure(Call<CreateBatchBody> call, Throwable t) {
                System.out.println("Failure. Call:\n" + call.toString() + ",\nThrowable:\n" + t.toString());
            }
        });

//        @Override
//        public void onResponse(Call<String> call, Response<String> response) {
//        System.out.println("Success" + response.body());
//    }
//
//        @Override
//        public void onFailure(Call<String> call, Throwable t) {
//
//    }

    }
}
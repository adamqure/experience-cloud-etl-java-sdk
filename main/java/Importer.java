
import Tools.Validator;
import ToolsInterfaces.CataloguerInterface;
import ToolsInterfaces.IngestorInterface;
import ToolsInterfaces.ValidatorInterface;
import com.google.gson.Gson;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import retrofit2.Call;
import retrofit2.Response;

import javax.security.auth.callback.Callback;
import ParameterClasses.Abstracts.DataSetIdInterface;
import ParameterClasses.Abstracts.SchemaInterface;
import ParameterClasses.Classes.AuthInfo;
import Tools.Cataloguer;
import Tools.Ingestor;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.*;

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

    public Importer() {
        Gson deserializer = new Gson();
        File config = new File("config.json");
        authInfo = null;
        try
        {
            String text = new Scanner(config).useDelimiter("\\A").next();
            authInfo = deserializer.fromJson(text, AuthInfo.class);
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

    public AuthInfo getAuthInfo() {
        return authInfo;
    }

    public void Upload(FileInputStream inputStream, SchemaInterface schema, DataSetIdInterface dsId)
    {
        dataIngestor.Upload(inputStream, schema, dsId, authInfo.getAccessToken());
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
            System.out.println(keyString);
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
        metas.put("https://ims-na1.adobelogin.com/s/meta_scope", Boolean.TRUE);
        String Jwt = Jwts.builder()
                .setIssuer(authInfo.getImsOrgId())
                .setExpiration(exp)
                .setSubject(authInfo.getSubject())
                .setAudience("https://ims-na1.adobelogin.com/c/" + authInfo.getApiKey())
                .addClaims(metas)
                .signWith(privKey, rs)
                .compact();
        System.out.println(Jwt);
        authInfo.setJwt(Jwt);
    }

    public String exchangeJwtAuth()
    {
        String authToken = "";
        Map<String, String> headers = new HashMap<>();
        headers.put("client_id", authInfo.getApiKey());
        headers.put("client_secret", authInfo.getClientSecret());
        headers.put("jwt_token", authInfo.getJwt());
        Call<Void> call = API.getAuthService().getAuthToken(headers, authInfo);
//        call.enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//
//            }
//        });
        return authToken;
    }
}
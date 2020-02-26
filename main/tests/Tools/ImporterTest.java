package Tools;

import Exceptions.*;
import org.junit.jupiter.api.*;

import java.io.FileNotFoundException;
import java.security.InvalidKeyException;

class ImporterTest
{
    Importer testImp;
    String DataSetID = "5e29e7e984479018a93e70a7";
    String ExpiredToken = "eyJ4NXUiOiJpbXNfbmExLWtleS0xLmNlciIsImFsZyI6IlJTMjU2In0.eyJpZCI6IjE1ODA3NTU5MzM0OTFfNTM3ZDZi" +
            "YWQtNmJjOS00YWFjLWEwZTktMDEzM2YyOTFiNzYxX3VlMSIsImNsaWVudF9pZCI6ImQ4YjY1Y2E3NWVlNDRiOGNhOWJmODdiNmZkYzBhMT" +
            "c0IiwidXNlcl9pZCI6IkQ5Q0I3OEEyNURBRTE0QkMwQTQ5NUMyMUB0ZWNoYWNjdC5hZG9iZS5jb20iLCJzdGF0ZSI6IntcInNlc3Npb25cI" +
            "jpcImh0dHBzOi8vaW1zLW5hMS5hZG9iZWxvZ2luLmNvbS9pbXMvc2Vzc2lvbi92MS9aakExT1daak1Ea3RabU5tWmkwME1qTTFMV0V5WkRR" +
            "dFlqUmlNREV3TnpOalpXTTBMUzFFT1VOQ056aEJNalZFUVVVeE5FSkRNRUUwT1RWRE1qRkFkR1ZqYUdGalkzUXVZV1J2WW1VdVkyOXRcIn0" +
            "iLCJ0eXBlIjoiYWNjZXNzX3Rva2VuIiwiYXMiOiJpbXMtbmExIiwiZmciOiJVRkFLWUpFMkhQRTVKUFVQRzZBTFlQUUFEVT09PT09PSIsIm" +
            "1vaSI6IjU0YmVlZjg3IiwiYyI6IlRrZnhXK3dwRStzZk9TbGc1RlZITlE9PSIsImV4cGlyZXNfaW4iOiI4NjQwMDAwMCIsInNjb3BlIjoib" +
            "3BlbmlkLHNlc3Npb24sQWRvYmVJRCxyZWFkX29yZ2FuaXphdGlvbnMsYWRkaXRpb25hbF9pbmZvLnByb2plY3RlZFByb2R1Y3RDb250ZXh0" +
            "IiwiY3JlYXRlZF9hdCI6IjE1ODA3NTU5MzM0OTEifQ.psJCN3iFkzMx9bgkQBB4cDBHzvuHK6eLT146iw1z-89kf0m0iPqshJuX3ddToUWp" +
            "3hXEbZWkr9Ta1BezbjTvSnpgtYbNFAs4M2mYnVHpzqCgJQxI41JzQKHAqj94_dHNJIWvHJERnME1L9dX0DHSmFSTSZVwOUZWT7HFdZg-2wP" +
            "TG4wY3VRVmiwVmmW3lQAJ5aL6N7O1rWUqEEb9tXHM9UJSKeFTdlsmyAX_MV9TK9-zB5kDpkhMK41rQiwUVWzCkB1gawJPutweGv5GiUieOO" +
            "lwLz0GfD5oH5aoA8FYXt9_hFziQPP55yVoxbYWuOPFMiqRBWmL_zbne8D4Kn7Uwg86399989";
    String invalidToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0" +
            "IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
    @BeforeEach
    void setUp()
    {
        testImp = new Importer();
    }

    @AfterEach
    void tearDown()
    {
    }

    @Test
    void upload()
    {

    }
/** SECTION: CREATEJWT TESTS */
    //This test checks that with a proper config, a valid JWT and Access token are made
    @Test
    void createJwtAndExchange()
    {
        System.out.println("in create and exchange test");
        try
        {
            testImp.createJwt();
            testImp.exchangeJwtAuth();
            while (testImp.getAuthInfo().getAccessToken() == "")
            {
                try
                {
                    Thread.sleep(1000);
                } catch (InterruptedException e)
                {
                    Thread.currentThread().interrupt();
                }
            }
            assert testImp.getAuthInfo().getAccessToken() != "" && testImp.getAuthInfo().getExpiration() != null;
        }
        catch (ParameterException | InvalidExchangeException e)
        {
            e.printStackTrace();
            System.out.println("Error when attempting to create JWT before proper Exchange test");
            Assertions.assertTrue(false);
        }
    }

    //Attempt to create a JWT with a null API Key
    @Test
    void createJwtnNullAPIKey()
    {
        System.out.println("in createJWTNullAPI test");
        testImp.getAuthInfo().setApiKey(null);
        Assertions.assertThrows(ParameterException.class, () -> {
            testImp.createJwt();
        });
    }

    @Test
    void createJwtEmptyAPIKey()
    {
        System.out.println("in createJWTEmptyAPI");
        testImp.getAuthInfo().setApiKey("");
        Assertions.assertThrows(ParameterException.class, () ->{
           testImp.createJwt();
        });
    }

    //Attempt to create a JWT with an empty string for RSA key
    @Test
    void createJwtEmptyRSAKey()
    {
        System.out.println("in createJWTEmptyRSA test");
        testImp.getAuthInfo().setRsaKey("");
        Assertions.assertThrows(ParameterException.class, () -> {
            testImp.createJwt();
        });
    }

    //Attempt to create a JWT with an empty string for RSA key
    @Test
    void createJwtNullRSAKey()
    {
        System.out.println("in createJWTNullRSA test");
        testImp.getAuthInfo().setRsaKey(null);
        Assertions.assertThrows(ParameterException.class, () -> {
            testImp.createJwt();
        });
    }

    //Attempt to create a JWT with null for the IMSOrgId
    //Should either throw an error when beginning the creation
    //OR return an error when the API call is made and the response fails
    @Test
    void createJwtNullImsId()
    {
        System.out.println("in createJWTnullIMS test");
        testImp.getAuthInfo().setImsOrgId(null);
        Assertions.assertThrows(ParameterException.class, () -> {
            testImp.createJwt();
        });
    }

    //Attempt to create a JWT with an empty string for the IMSOrgId
    //Should either throw an error when beginning the creation
    //OR return an error when the API call is made and the response fails
    @Test
    void createJwtEmptyImsId()
    {
        System.out.println("in createJWTemptyIMS test");
        testImp.getAuthInfo().setImsOrgId("");
        Assertions.assertThrows(ParameterException.class, () -> {
            testImp.createJwt();
        });
    }

    //Attempt to create a JWT with null for the Subject
    //Should either throw an error when beginning the creation
    //OR return an error when the API call is made and the response fails
    @Test
    void createJwtnullSub()
    {
        System.out.println("in createJWTNullSub test");
        testImp.getAuthInfo().setSubject(null);
        Assertions.assertThrows(ParameterException.class, () -> {
            testImp.createJwt();
        });
    }

    //Attempt to create a JWT with an empty string for the Subject
    //Should either throw an error when beginning the creation
    //OR return an error when the API call is made and the response fails
    @Test
    void createJwtEmptySub()
    {
        System.out.println("in createJWTEmptySub test");
        testImp.getAuthInfo().setSubject("");
        Assertions.assertThrows(ParameterException.class, () -> {
            testImp.createJwt();
        });
    }
/** SECTION: EXCHANGEJWT TESTS */
    //Attempt to exchange an empty string JWT
    //Should not even attempt the exchange. Should print
    //an error message and throw an exception
    @Test
    void exchangeEmptyJwt()
    {
        System.out.println("in ExchangeEmptyJWT test");
        testImp.getAuthInfo().setJwt("");
        Assertions.assertThrows(ParameterException.class, ()->{
            testImp.exchangeJwtAuth();
        });
    }

    //Attempt to exchange a jwt that is null
    //Should print an error message and throw an exception
    @Test
    void exchangeNullJwt()
    {
        System.out.println("in ExchangeNullJWT test");
        testImp.getAuthInfo().setJwt(null);
        Assertions.assertThrows(ParameterException.class, ()->{
            testImp.exchangeJwtAuth();
        });
    }

    @Test
    void exchangeInvalidJWT()
    {
        System.out.println("in exchangeInvalidJWT");
        testImp.getAuthInfo().setJwt(invalidToken);
        Assertions.assertThrows(InvalidExchangeException.class, ()->{
            testImp.exchangeJwtAuth();
        });
    }

    //TODO Async in order to wait for API call since we will not know it is expired until the request has completed
    @Test
    void exchangeExpiredJwt()
    {
        System.out.println("in ExchangeExpiredJWT test");
        testImp.getAuthInfo().setJwt(ExpiredToken);
        Assertions.assertThrows(InvalidExchangeException.class, ()->{
           testImp.exchangeJwtAuth();
        });
    }

    //This test should send a request with an empty string for the api key and a
    //failure response should come back and the exchangeJwtAuth method should
    //print an error message and throw some kind of exception
    @Test
    void exchangeJwtEmptyApiKey()
    {
        System.out.println("in ExchangeEmptyAPIKey test");
        testImp.getAuthInfo().setApiKey("");
        Assertions.assertThrows(ParameterException.class, ()->{
            testImp.exchangeJwtAuth();
        });
    }

    @Test
    void exchangeJwtNullApiKey()
    {
        System.out.println("in ExchangeNullAPIKey test");
        testImp.getAuthInfo().setApiKey(null);
        Assertions.assertThrows(ParameterException.class, ()->{
            testImp.exchangeJwtAuth();
        });
    }

    //This test should send a request with an empty string for the client secret
    //As above, the response should have an error code and the function should throw
    //an exception detailing the issue
    @Test
    void exchangeJwtEmptyClientSecret()
    {
        System.out.println("in ExchangeEmptyClientSecret test");
        testImp.getAuthInfo().setClientSecret("");
        Assertions.assertThrows(ParameterException.class, ()->{
            testImp.exchangeJwtAuth();
        });
    }

/** SECTION: UPLOADFILE TESTS */
    //Attempt to upload a file with an empty string for filename
    //An error should be thrown when the file is not found with a
    //corresponding error message
    @Test
    void uploadEmptyFilename()
    {
        try
        {
            testImp.createJwt();
            testImp.exchangeJwtAuth();
            Assertions.assertThrows(ParameterException.class, () ->
            {
                testImp.uploadFile("", null, DataSetID);
            });
        }
        catch (ParameterException | InvalidExchangeException e)
        {
            e.printStackTrace();
            System.out.println("Error in createJWT when attempting uploadEmptyFilename test");
            Assertions.assertTrue(false);
        }
    }

    //Attempt to upload a file with a null for filename
    //An error should be thrown when attempt to call File fails
    //with corresponding error message
    @Test
    void uploadNullFilename()
    {
        try
        {
            testImp.createJwt();
            testImp.exchangeJwtAuth();
            Assertions.assertThrows(ParameterException.class, () ->
            {
                testImp.uploadFile(null, null, DataSetID);
            });
        }
        catch (ParameterException | InvalidExchangeException e)
        {
            e.printStackTrace();
            System.out.println("Error in createJWT when attempting uploadNullFilename test");
            Assertions.assertTrue(false);
        }
    }

    //Attempt to upload a file with an empty string for DataSetID
    //An error should be thrown with a corresponding error message
    @Test
    void uploadEmptyDSId()
    {
        try
        {
            testImp.createJwt();
            testImp.exchangeJwtAuth();
            Assertions.assertThrows(ParameterException.class, () ->
            {
                testImp.uploadFile("Tools/test128.json", null, "");
            });
        }
        catch (ParameterException | InvalidExchangeException e)
        {
            e.printStackTrace();
            System.out.println("Error in createJWT when attempting uploadEmptyDSId test");
            Assertions.assertTrue(false);
        }
    }

    //Attempt to upload a file with a null for filename
    //An error should be thrown when attempt to call File fails
    //with corresponding error message
    @Test
    void uploadNullDataSetID()
    {
        try
        {
            testImp.createJwt();
            testImp.exchangeJwtAuth();
            Assertions.assertThrows(ParameterException.class, () ->
            {
                testImp.uploadFile("test128.json", null, null);
            });
        }
        catch (ParameterException | InvalidExchangeException e)
        {
            e.printStackTrace();
            System.out.println("Error in createJWT when attempting uploadNullDSId test");
            Assertions.assertTrue(false);
        }
    }

    @Test
    void uploadValidSmallFile()
    {
        try
        {
            testImp.createJwt();
            testImp.exchangeJwtAuth();
            Assertions.assertDoesNotThrow(()->{
                testImp.uploadFileSync("test128.json", null, DataSetID);
            });
        }
        catch (ParameterException | InvalidExchangeException e)
        {
            e.printStackTrace();
            System.out.println("Error in creation and exchange of JWT when attempting valid upload");
            Assertions.assertTrue(false);
        }
    }
}
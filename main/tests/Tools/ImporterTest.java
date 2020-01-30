package Tools;

import org.junit.jupiter.api.*;

import java.io.FileNotFoundException;
import java.security.InvalidKeyException;
import java.security.spec.InvalidKeySpecException;

class ImporterTest {
    Importer testImp;

    @BeforeEach
    void setUp() {
        testImp = new Importer();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void upload()
    {

    }

    //This test checks that with a proper config, a valid JWT and Access token are made
    @Test
    void createJwtAndExchange()
    {
        testImp.createJwt();
        while (testImp.getAuthInfo().getAccessToken() == "")
        {
            try
            { Thread.sleep(1000); }
            catch (InterruptedException e)
            { Thread.currentThread().interrupt(); }
        }
        assert testImp.getAuthInfo().getAccessToken() != "" && testImp.getAuthInfo().getExpiration() != null;
    }

    //Attempt to create a JWT with a null API Key
    @Test
    void createJwtnNullAPIKey()
    {
        testImp.getAuthInfo().setApiKey(null);
        Assertions.assertThrows(Exception.class, () -> {
            testImp.createJwt();
        });
    }

    //Attempt to create a JWT with an empty string for RSA key
    @Test
    void createJwtEmptyRSAKey()
    {
        testImp.getAuthInfo().setRsaKey("");
        Assertions.assertThrows(InvalidKeyException.class, () -> {
            testImp.createJwt();
        });
    }

    //Attempt to create a JWT with null for the IMSOrgId
    //Should either throw an error when beginning the creation
    //OR return an error when the API call is made and the response fails
    @Test
    void createJwtNullImsId()
    {
        testImp.getAuthInfo().setImsOrgId(null);
        Assertions.assertThrows(Exception.class, () -> {
            testImp.createJwt();
        });
    }

    //Attempt to create a JWT with an empty string for the IMSOrgId
    //Should either throw an error when beginning the creation
    //OR return an error when the API call is made and the response fails
    @Test
    void createJwtEmptyImsId()
    {
        testImp.getAuthInfo().setImsOrgId("");
        Assertions.assertThrows(Exception.class, () -> {
            testImp.createJwt();
        });
    }

    //Attempt to create a JWT with null for the Subject
    //Should either throw an error when beginning the creation
    //OR return an error when the API call is made and the response fails
    @Test
    void createJwtnullSub()
    {
        testImp.getAuthInfo().setSubject(null);
        Assertions.assertThrows(Exception.class, () -> {
            testImp.createJwt();
        });
    }

    //Attempt to create a JWT with an empty string for the Subject
    //Should either throw an error when beginning the creation
    //OR return an error when the API call is made and the response fails
    @Test
    void createJwtEmptySub()
    {
        testImp.getAuthInfo().setSubject("");
        Assertions.assertThrows(Exception.class, () -> {
            testImp.createJwt();
        });
    }

    //Attempt to exchange an empty string JWT
    //Should not even attempt the exchange. Should print
    //an error message and throw an exception
    @Test
    void exchangeEmptyJwt()
    {
        testImp.getAuthInfo().setJwt("");
        Assertions.assertThrows(Exception.class, ()->{
            testImp.exchangeJwtAuth();
        });
    }

    //Attempt to exchange a jwt that is null
    //Should print an error message and throw an exception
    @Test
    void exchangeNullJwt()
    {
        testImp.getAuthInfo().setJwt(null);
        Assertions.assertThrows(Exception.class, ()->{
            testImp.exchangeJwtAuth();
        });
    }

    @Test
    void exchangeExpiredJwt()
    {
        Assertions.assertThrows(Exception.class, ()->{
           testImp.exchangeJwtAuth();
        });
    }

    //This test should send a request with an empty string for the api key and a
    //failure response should come back and the exchangeJwtAuth method should
    //print an error message and throw some kind of exception
    @Test
    void exchangeJwtEmptyApiKey()
    {
        testImp.getAuthInfo().setApiKey("");
        Assertions.assertThrows(Exception.class, ()->{
            testImp.exchangeJwtAuth();
        });
    }

    //This test should send a request with an empty string for the client secret
    //As above, the response should have an error code and the function should throw
    //an exception detailing the issue
    @Test
    void exchangeJwtEmptyClientSecret()
    {
        testImp.getAuthInfo().setClientSecret("");
        Assertions.assertThrows(Exception.class, ()->{
            testImp.exchangeJwtAuth();
        });
    }


    //Attempt to upload a file with an empty string for filename
    //An error should be thrown when the file is not found with a
    //corresponding error message
    @Test
    void uploadEmptyFilename()
    {
        testImp.createJwt();
        testImp.exchangeJwtAuth();
        Assertions.assertThrows(FileNotFoundException.class, ()->{
            testImp.uploadFile("", null, "5e29e7e984479018a93e70a7");
        });
    }
}
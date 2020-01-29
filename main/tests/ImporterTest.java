import ParameterClasses.Classes.AuthInfo;
import ParameterClasses.Classes.AuthToken;
import org.junit.Rule;
import org.junit.jupiter.api.*;
import org.junit.rules.ExpectedException;

import java.awt.event.ItemEvent;
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
        testImp.exchangeJwtAuth();
        while (testImp.getAuthInfo().getAccessToken() == "")
        {
            try
            { Thread.sleep(1000); }
            catch (InterruptedException e)
            { Thread.currentThread().interrupt(); }
        }
        assert testImp.getAuthInfo().getAccessToken() != "" && testImp.getAuthInfo().getExpiration() != null;
    }

    @Test
    void createJwtnNullAPIKey()
    {
        testImp.getAuthInfo().setApiKey(null);
        testImp.createJwt();
    }

    @Test
    void createJwtNullRSAKey()
    {
        testImp.getAuthInfo().setRsaKey(null);
        testImp.createJwt();
    }

    @Test
    void createJwtNullImsId()
    {
        testImp.getAuthInfo().setImsOrgId(null);
        testImp.createJwt();
    }

    @Test
    void createJwtnullSub()
    {
        testImp.getAuthInfo().setSubject(null);
        testImp.createJwt();
    }

    @Test
    void uploadNull
}
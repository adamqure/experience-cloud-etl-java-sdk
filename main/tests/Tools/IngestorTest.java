package Tools;

import ParameterClasses.AuthInfo;
import ParameterClasses.AuthToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IngestorTest {
    Ingestor testIng;
    AuthInfo testAuth;
    @BeforeEach
    void setUp() {
        testIng = new Ingestor();
        Importer tempImp = new Importer();
        tempImp.createJwt();
        tempImp.exchangeJwtAuth();
        testAuth = tempImp.getAuthInfo();
    }

    @AfterEach
    void tearDown() {
    }

    //Attempt to create a batch without a dataset ID
    @Test
    void createBatchEmptyDSID()
    {
        Assertions.assertThrows(Exception.class, ()->{
           testIng.createBatch(testAuth, "");
        });
    }

    //Attempt to create a batch with null in the DatasetID
    @Test
    void createBatchNullDSID()
    {
        Assertions.assertThrows(Exception.class, ()->{
           testIng.createBatch(testAuth, null);
        });
    }

    //Attempt to create a batch with a null authInfo Object
    @Test
    void createBatchNullAuthInfo()
    {
        Assertions.assertThrows(Exception.class, ()->{
           testIng.createBatch(null, "5e29e7e984479018a93e70a7");
        });
    }

    //Attempt to create a batch with an empty string in Access Token
    @Test
    void createBatchEmptyAuthToken()
    {
        testAuth.setAccessToken(new AuthToken());
        Assertions.assertThrows(Exception.class, ()->{
            testIng.createBatch(testAuth, "5e29e7e984479018a93e70a7");
        });
    }

    //Test creating a batch with an empty string in IMS Org
    @Test
    void createBatchEmptyImsOrg()
    {
        testAuth.setImsOrgId("");
        Assertions.assertThrows(Exception.class, ()->{
            testIng.createBatch(testAuth, "5e29e7e984479018a93e70a7");
        });
    }

    //Test creating a batch with a null in API key
    @Test
    void createBatchNullAPI()
    {
        testAuth.setApiKey(null);
        Assertions.assertThrows(Exception.class, ()->{
            testIng.createBatch(testAuth, "5e29e7e984479018a93e70a7");
        });
    }

    //Test creating a batch with an empty string in API key
    @Test
    void createBatchEmptyAPIkey()
    {
        testAuth.setApiKey("");
        Assertions.assertThrows(Exception.class, ()->{
            testIng.createBatch(testAuth, "5e29e7e984479018a93e70a7");
        });
    }

    //Test creating a batch with a null in IMS Org
    @Test
    void createBatchNullImsOrg()
    {
        testAuth.setImsOrgId(null);
        Assertions.assertThrows(Exception.class, ()->{
            testIng.createBatch(testAuth, "5e29e7e984479018a93e70a7");
        });
    }

    //Attempt to create a batch with an authInfo object that
    //does not have the information needed
    @Test
    void createBatchEmptyAuthInfo()
    {
        Assertions.assertThrows(Exception.class, ()-> {
            testIng.createBatch(new AuthInfo("", "", ""),
                    "5e29e7e984479018a93e70a7");
        });
    }

    //Attempt to upload to an already created batch with an empty string for filename
    @Test
    void uploadFileEmptyFilename()
    {
        String batchID = testIng.createBatch(testAuth, "5e29e7e984479018a93e70a7");
        Assertions.assertThrows(Exception.class, ()->{
            testIng.uploadFileToBatch(testAuth, null, batchID, "5e29e7e984479018a93e70a7", "");
        });
    }

    //Attempt to upload to an already created batch with a null filename
    @Test
    void uploadFileNullFilename()
    {
        String batchID = testIng.createBatch(testAuth, "5e29e7e984479018a93e70a7");
        Assertions.assertThrows(Exception.class, ()->{
            testIng.uploadFileToBatch(testAuth, null, batchID, "5e29e7e984479018a93e70a7", null);
        });
    }

    @Test
    void generateHeadersEmptyAuthInfo()
    {

    }
}
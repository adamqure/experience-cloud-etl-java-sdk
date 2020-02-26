package Tools;

import Exceptions.*;
import ParameterClasses.AuthInfo;
import ParameterClasses.AuthToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class IngestorTest
{
    Ingestor testIng;
    AuthInfo testAuth;
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
    String test128 = "test128.json";
    String test256 = "test256.json";
    String test500 = "test500.json";
    @BeforeEach
    void setUp()
    {
        try
        {
            testIng = new Ingestor();
            Importer tempImp = new Importer();
            tempImp.createJwt();
            tempImp.exchangeJwtAuth();
            testAuth = tempImp.getAuthInfo();
        }
        catch(ParameterException | InvalidExchangeException e)
        {
            e.printStackTrace();

        }
    }

    @AfterEach
    void tearDown()
    {
    }
/** SECTION, CREATE BATCH TESTS */
    //Attempt to create a batch without a dataset ID
    @Test
    void createBatchEmptyDSID()
    {
        Assertions.assertThrows(ParameterException.class, ()->{
           testIng.createBatch(testAuth, "");
        });
    }

    //Attempt to create a batch with null in the DatasetID
    @Test
    void createBatchNullDSID()
    {
        Assertions.assertThrows(ParameterException.class, ()->{
           testIng.createBatch(testAuth, null);
        });
    }

    //Test creating a batch with a null in API key
    @Test
    void createBatchNullAPIKey()
    {
        testAuth.setApiKey(null);
        Assertions.assertThrows(ParameterException.class, ()->{
            testIng.createBatch(testAuth, DataSetID);
        });
    }

    //Test creating a batch with an empty string in API key
    @Test
    void createBatchEmptyAPIkey()
    {
        testAuth.setApiKey("");
        Assertions.assertThrows(ParameterException.class, ()->{
            testIng.createBatch(testAuth, DataSetID);
        });
    }

    //Test creating a batch with an empty string in IMS Org
    @Test
    void createBatchEmptyImsOrg()
    {
        testAuth.setImsOrgId("");
        Assertions.assertThrows(ParameterException.class, ()->{
            testIng.createBatch(testAuth, DataSetID);
        });
    }

    //Test creating a batch with a null in IMS Org
    @Test
    void createBatchNullImsOrg()
    {
        testAuth.setImsOrgId(null);
        Assertions.assertThrows(ParameterException.class, ()->{
            testIng.createBatch(testAuth, DataSetID);
        });
    }

    //Attempt to create a batch with an authInfo object that
    //does not have the information needed
    @Test
    void createBatchEmptyAuthInfo()
    {
        Assertions.assertThrows(ParameterException.class, ()-> {
            testIng.createBatch(new AuthInfo("", "", ""),
                    DataSetID);
        });
    }

    //Attempt to create a batch with a null authInfo Object
    @Test
    void createBatchNullAuthInfo()
    {
        Assertions.assertThrows(ParameterException.class, ()->{
            testIng.createBatch(null, DataSetID);
        });
    }

    //Attempt to create a batch with an empty string in Access Token
    @Test
    void createBatchEmptyAuthToken()
    {
        testAuth.setAccessToken(new AuthToken());
        Assertions.assertThrows(ParameterException.class, ()->{
            testIng.createBatch(testAuth, DataSetID);
        });
    }

/** SECTION, UPLOAD FILE TESTS */
    //Attempt to upload to an already created batch with an empty string for filename
    @Test
    void uploadFileEmptyFilename()
    {
        try
        {
            String batchID = testIng.createBatch(testAuth, DataSetID);
            Assertions.assertThrows(ParameterException.class, () ->
            {
                testIng.addFileToBatch(testAuth, null, batchID, DataSetID, "", true);
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Error in createBatch while testing UploadFileEmptyFilename");
            Assertions.assertTrue(false);
        }
    }

    //Attempt to upload to an already created batch with a null filename
    @Test
    void uploadFileNullFilename()
    {
        try
        {
            String batchID = testIng.createBatch(testAuth, DataSetID);
            Assertions.assertThrows(ParameterException.class, () ->
            {
                testIng.addFileToBatch(testAuth, null, batchID, DataSetID, null, true);
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Error in createBatch while testing UploadFileNullFilename");
            Assertions.assertTrue(false);
        }
    }

    //Attempt to upload to an already created batch with an empty string in batchID
    @Test
    void uploadFileEmptyBatchID()
    {
        String batchID = "";
        Assertions.assertThrows(ParameterException.class, ()->{
            testIng.addFileToBatch(testAuth, null, batchID, DataSetID, null, true);
        });
    }

    //Attempt to upload to an already created batch with a null in batchID
    @Test
    void uploadFileNullBatchID()
    {
        String batchID = null;
        Assertions.assertThrows(ParameterException.class, ()->{
            testIng.addFileToBatch(testAuth, null, batchID, DataSetID, null, true);
        });
    }

    @Test
    void uploadValidFileSync()
    {
        try
        {
            String batchID = testIng.createBatch(testAuth, DataSetID);
            Assertions.assertDoesNotThrow(()->{
                testIng.addFileToBatch(testAuth, null, batchID, DataSetID, test128, true);
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Error when creating batch for valid file upload test");
            Assertions.assertTrue(false);
        }
    }
/** SECTION, CANCEL BATCH TESTS */
    @Test
    void cancelBatchEmptyAuthInfo()
    {
        try
        {
            String batchID = testIng.createBatch(testAuth, DataSetID);
            testAuth = new AuthInfo("", "", "");
            Assertions.assertThrows(ParameterException.class, () ->
            {
                testIng.cancelBatch(testAuth, batchID);
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Error in createBatch while testing CancelBatchEmptyAuthInfo");
            Assertions.assertTrue(false);
        }
    }

    @Test
    void cancelBatchNullAuthInfo()
    {
        try
        {
            String batchID = testIng.createBatch(testAuth, DataSetID);
            testAuth = null;
            Assertions.assertThrows(ParameterException.class, () ->
            {
                testIng.cancelBatch(testAuth, batchID);
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Error in createBatch while testing CancelBatchNullAuthInfo");
            Assertions.assertTrue(false);
        }
    }

    @Test
    void cancelBatchEmptyBatchID()
    {
        Assertions.assertThrows(ParameterException.class, ()->{
            testIng.cancelBatch(testAuth, "");
        });
    }

    @Test
    void cancelBatchNullBatchID()
    {
        Assertions.assertThrows(ParameterException.class, ()->{
            testIng.cancelBatch(testAuth, null);
        });
    }

}
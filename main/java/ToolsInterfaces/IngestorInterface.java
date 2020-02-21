package ToolsInterfaces;

import ParameterClasses.AuthInfo;
import ParameterClasses.AuthToken;
import ParameterClasses.Schema;

import java.io.FileInputStream;
import java.io.IOException;

/**This interface is used as a template for data ingestion on cloud platforms.
 * All parameters for all methods are interfaces, such that users may define their
 * own implementations for use, while still using the Tools.Importer base class to upload*/
public interface IngestorInterface {
    String createBatch(AuthInfo authInfo, String datasetId) throws IOException;
    boolean addFileToBatch(AuthInfo authInfo, Schema schema, String batchId, String dataSetId, String filename, boolean runSync) throws IOException;
    void signalBatchComplete(AuthInfo authInfo, String batchId) throws IOException;
    void cancelBatch(AuthInfo authInfo, String batchId) throws IOException;
}

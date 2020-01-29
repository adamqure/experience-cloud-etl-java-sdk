package ToolsInterfaces;

import ParameterClasses.AuthInfo;
import ParameterClasses.AuthToken;
import ParameterClasses.Schema;

import java.io.FileInputStream;

/**This interface is used as a template for data ingestion on cloud platforms.
 * All parameters for all methods are interfaces, such that users may define their
 * own implementations for use, while still using the Tools.Importer base class to upload*/
public interface IngestorInterface {
    String createBatch(AuthInfo authInfo, String datasetId);
    boolean uploadFileToBatch(AuthInfo authInfo, Schema schema, String batchId, String dataSetId, String filename);
    boolean signalBatchComplete(AuthInfo authInfo, String batchId);
    boolean cancelBatch(AuthInfo authInfo, String batchId);
}

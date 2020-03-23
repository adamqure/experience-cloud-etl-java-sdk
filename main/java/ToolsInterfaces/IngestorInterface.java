package ToolsInterfaces;

import ParameterClasses.AuthInfo;

import java.io.IOException;

/**
 * This interface is used as a template for data ingestion on cloud platforms.
 * All parameters for all methods are interfaces, such that users may define their
 * own implementations for use, while still using the Tools.Importer base class to upload
 */
public interface IngestorInterface {
    String createBatch(AuthInfo authInfo, String datasetId) throws IOException;
    boolean addFileToBatch(AuthInfo authInfo, String batchId, String datasetId, String filename) throws IOException;
    boolean addFileToBatchSync(AuthInfo authInfo, String batchId, String datasetId, String filename) throws IOException;
    void signalBatchComplete(AuthInfo authInfo, String batchId) throws IOException;
}

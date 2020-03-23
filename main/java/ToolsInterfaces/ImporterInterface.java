package ToolsInterfaces;

import ParameterClasses.*;

public interface ImporterInterface {
    String uploadFile(String filename, String datasetId);
    String uploadFileSync(String filename, String datasetId);
    String createBatch(String datasetId);
    void addFileToBatch(String batchId, String datasetId, String filename);
    void addFileToBatchSync(String batchId, String datasetId, String filename);
    void closeBatch(String batchId);
    String getBatchStatus(String batchId);
}

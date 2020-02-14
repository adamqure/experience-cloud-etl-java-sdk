package ToolsInterfaces;

import ParameterClasses.Schema;

public interface ImporterInterface {
    String uploadFile(String filename, Schema schema, String datasetId);
    String uploadFileSync(String filename, Schema schema, String datasetId);
    String getBatchStatus(String batchId);
}

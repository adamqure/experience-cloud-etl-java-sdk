package ToolsInterfaces;

import ParameterClasses.Schema;

public interface ImporterInterface {
    String uploadFile(String filename, Schema schema, String datasetId);
    void getBatchStatus(String batchId);
}

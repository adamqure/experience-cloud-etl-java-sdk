package ToolsInterfaces;

import Exceptions.ParameterException;
import ParameterClasses.Schema;

public interface ImporterInterface {
    String uploadFile(String filename, Schema schema, String datasetId) throws ParameterException;
    String uploadFileSync(String filename, Schema schema, String datasetId) throws ParameterException;
    String getBatchStatus(String batchId);
}

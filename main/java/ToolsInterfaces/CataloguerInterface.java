package ToolsInterfaces;

import ParameterClasses.AuthInfo;

public interface CataloguerInterface
{
    void createDataset();
    void getUploadStatus(AuthInfo authInfo, String batchId);
    void generateReport();
    void getBatchesList();
    void getDatasetByID();
}

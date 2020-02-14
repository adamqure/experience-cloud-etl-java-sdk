package ToolsInterfaces;

import ParameterClasses.AuthInfo;

public interface CataloguerInterface
{
    void createDataset();
    String getBatchStatus(AuthInfo authInfo, String batchId);
    void generateReport();
    void getBatchesList();
    void getDatasetByID();
}

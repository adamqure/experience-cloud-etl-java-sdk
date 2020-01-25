package ToolsInterfaces;

import ParameterClasses.Abstracts.DataSetIdInterface;
import ParameterClasses.Abstracts.SchemaInterface;
import java.io.FileInputStream;

/**This interface is used as a template for data ingestion on cloud platforms.
 * All parameters for all methods are interfaces, such that users may define their
 * own implementations for use, while still using the Importer base class to upload*/
public interface IngestorInterface
{
    public void Upload(FileInputStream toUpload, SchemaInterface schema,
                       DataSetIdInterface dataSetId, String authToken);
    void createBatch();
    void uploadFileToBatch();
    void signalBatchComplete();
    void cancelBatch();
}

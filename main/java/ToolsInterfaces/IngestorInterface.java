package java.ToolsInterfaces;

import java.ParameterClasses.AuthTokenInterface;
import java.ParameterClasses.DataSetIdInterface;
import java.ParameterClasses.SchemaInterface;
import java.io.FileInputStream;

/**This interface is used as a template for data ingestion on cloud platforms.
 * All parameters for all methods are interfaces, such that users may define their
 * own implementations for use, while still using the Importer base class to upload*/
public interface IngestorInterface
{
    public void Upload(FileInputStream toUpload, SchemaInterface schema,
                       DataSetIdInterface dataSetId, AuthTokenInterface authToken);
}

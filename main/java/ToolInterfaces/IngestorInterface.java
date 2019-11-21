package java.ToolInterfaces;

import java.ParameterInterfaces.*;
import java.io.FileInputStream;

/**This interface is used as a template for data ingestion on cloud platforms.
 * All parameters for all methods are interfaces, such that users may define their
 * own implementations for use, while still using the Importer base class to upload*/
public interface IngestorInterface
{
    public void Upload(FileInputStream toUpload, SchemaInterface schema,
                       DataSetIdInterface dataSetId, AuthInfoInterface authInfo);
}

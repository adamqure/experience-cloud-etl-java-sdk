package java.AEPTools;

import java.ParameterInterfaces.*;
import java.ToolInterfaces.IngestorInterface;
import java.io.FileInputStream;

/**
 * Basic AEPIngestor that implements the IngestorInterface
 */
public class AEPIngestor implements IngestorInterface
{

    @Override
    public void Upload(FileInputStream toUpload, SchemaInterface schema,
                       DataSetIdInterface dataSetId, AuthInfoInterface authInfo) {

    }
}

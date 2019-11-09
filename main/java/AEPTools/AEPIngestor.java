package java.AEPTools;

import java.ParameterInterfaces.AuthTokenInterface;
import java.ParameterInterfaces.DataSetIdInterface;
import java.ParameterInterfaces.SchemaInterface;
import java.ToolInterfaces.IngestorInterface;
import java.io.FileInputStream;

/**
 * Basic AEPIngestor that implements the IngestorInterface
 */
public class AEPIngestor implements IngestorInterface
{

    @Override
    public void Upload(FileInputStream toUpload, SchemaInterface schema, DataSetIdInterface dataSetId, AuthTokenInterface authToken) {

    }
}

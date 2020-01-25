package Tools;

import ParameterClasses.Abstracts.DataSetIdInterface;
import ParameterClasses.Abstracts.SchemaInterface;
import ToolsInterfaces.IngestorInterface;
import java.io.FileInputStream;

/**
 * Basic Ingestor that implements the IngestorInterface
 */
public class Ingestor implements IngestorInterface
{
    @Override
    public void Upload(FileInputStream toUpload, SchemaInterface schema, DataSetIdInterface dataSetId, String authToken) {

    }

    @Override
    public void createBatch(){

    }

    @Override
    public void uploadFileToBatch(){

    }

    @Override
    public void signalBatchComplete(){

    }

    @Override
    public void cancelBatch(){

    }
}

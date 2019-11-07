package java;

import java.ParameterClasses.AuthTokenInterface;
import java.ParameterClasses.DataSetIdInterface;
import java.ParameterClasses.SchemaInterface;
import java.Interfaces.IngestorInterface;
import java.io.FileInputStream;

/**
 * Basic Ingestor that implements the IngestorInterface
 */
public class Ingestor implements IngestorInterface
{

    @Override
    public void Upload(FileInputStream toUpload, SchemaInterface schema, DataSetIdInterface dataSetId, AuthTokenInterface authToken) {

    }
}

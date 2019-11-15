package java.ToolInterfaces;

import java.ParameterInterfaces.AuthTokenInterface;
import java.ParameterInterfaces.DataSetIdInterface;
import java.ParameterInterfaces.SchemaInterface;

/**
 * Interface to be used to create a Schema AEPValidator
 */
public interface ValidatorInterface {

    public void validateSchema(SchemaInterface schema, DataSetIdInterface dataSetId, AuthTokenInterface authToken);
}

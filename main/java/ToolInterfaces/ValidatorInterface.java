package java.ToolInterfaces;

import java.ParameterInterfaces.*;

/**
 * Interface to be used to create a Schema AEPValidator
 */
public interface ValidatorInterface {

    public void validateSchema(SchemaInterface schema, DataSetIdInterface dataSetId, AuthInfoInterface authInfo);
}

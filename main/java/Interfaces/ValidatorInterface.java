package java.Interfaces;

import java.ParameterClasses.AuthTokenInterface;
import java.ParameterClasses.DataSetIdInterface;
import java.ParameterClasses.SchemaInterface;

/**
 * Interface to be used to create a Schema Validator
 */
public interface ValidatorInterface {

    public void validateSchema(SchemaInterface schema, DataSetIdInterface dataSetId, AuthTokenInterface authToken);
}

package java;

import java.ParameterClasses.AuthTokenInterface;
import java.ParameterClasses.DataSetIdInterface;
import java.ParameterClasses.SchemaInterface;
import java.Interfaces.ValidatorInterface;

/**
 * Basic validator that implements the validator interface
 */
public class Validator implements ValidatorInterface
{

    @Override
    public void validateSchema(SchemaInterface schema, DataSetIdInterface dataSetId, AuthTokenInterface authToken) {

    }
}

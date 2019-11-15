package java.AEPTools;

import java.ParameterInterfaces.AuthTokenInterface;
import java.ParameterInterfaces.DataSetIdInterface;
import java.ParameterInterfaces.SchemaInterface;
import java.ToolInterfaces.ValidatorInterface;

/**
 * Basic validator that implements the validator interface
 */
public class AEPValidator implements ValidatorInterface
{

    @Override
    public void validateSchema(SchemaInterface schema, DataSetIdInterface dataSetId, AuthTokenInterface authToken) {

    }
}

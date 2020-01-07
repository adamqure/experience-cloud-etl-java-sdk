package Tools;

import ParameterClasses.Abstracts.AuthTokenInterface;
import ParameterClasses.Abstracts.DataSetIdInterface;
import ParameterClasses.Abstracts.SchemaInterface;
import ToolsInterfaces.ValidatorInterface;

/**
 * Basic validator that implements the validator interface
 */
public class Validator implements ValidatorInterface
{

    @Override
    public void validateSchema(SchemaInterface schema, DataSetIdInterface dataSetId, AuthTokenInterface authToken) {

    }
    @Override
    public void createSchema(){

    }
    @Override
    public void lookupSchema(){

    }
    @Override
    public void getTenantId(){

    }
}

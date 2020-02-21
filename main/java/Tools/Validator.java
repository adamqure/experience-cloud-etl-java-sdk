package Tools;

import ParameterClasses.AuthInfo;
import ParameterClasses.Schema;
import ToolsInterfaces.ValidatorInterface;

/**
 * Basic validator that implements the validator interface
 */
public class Validator implements ValidatorInterface {

    @Override
    public void validateSchema(Schema schema, String dataSetId, AuthInfo authInfo) {

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

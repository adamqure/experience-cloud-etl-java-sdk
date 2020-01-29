package ToolsInterfaces;

import ParameterClasses.AuthInfo;
import ParameterClasses.Schema;

/**
 * Interface to be used to create a Schema Validator
 */
public interface ValidatorInterface {

    void validateSchema(Schema schema, String dataSetId, AuthInfo authInfo);
    void createSchema();
    void lookupSchema();
    void getTenantId();
}

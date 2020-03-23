package ToolsInterfaces;

import ParameterClasses.*;

/**
 * Interface to be used to create a Schema Validator
 */
public interface ValidatorInterface {

    void validateSchema(String dataSetId, AuthInfo authInfo);
    void createSchema();
    void lookupSchema();
    void getTenantId();
}

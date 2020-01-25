package ToolsInterfaces;

import ParameterClasses.Abstracts.AuthTokenInterface;
import ParameterClasses.Abstracts.DataSetIdInterface;
import ParameterClasses.Abstracts.SchemaInterface;

/**
 * Interface to be used to create a Schema Validator
 */
public interface ValidatorInterface {

    void validateSchema(SchemaInterface schema, DataSetIdInterface dataSetId, AuthTokenInterface authToken);
    void createSchema();
    void lookupSchema();
    void getTenantId();
}

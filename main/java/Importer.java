package java;

import java.Interfaces.*;

/**
 * Basic importer class that implements the ImporterInterface
 * Importer uses an ingestor, validator, and cataloguer
 */
public class Importer
{
    private IngestorInterface dataIngestor;
    private CataloguerInterface dataCataloguer;
    private ValidatorInterface schemaValidator;

    public Importer()
    {
        dataCataloguer = new Cataloguer();
        dataIngestor = new Ingestor();
        schemaValidator = new Validator();
    }


}
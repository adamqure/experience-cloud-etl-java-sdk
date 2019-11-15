package java;

import java.AEPTools.AEPCataloguer;
import java.AEPTools.AEPIngestor;
import java.AEPTools.AEPValidator;
import java.ToolsInterfaces.*;

/**
 * Basic importer class that implements the ImporterInterface
 * Importer uses an ingestor, validator, and cataloguer
 */
public class Importer
{
    private IngestorInterface dataIngestor;
    private CataloguerInterface dataCataloguer;
    private ValidatorInterface schemaValidator;

    public Importer() {
        dataCataloguer = new AEPCataloguer();
        dataIngestor = new AEPIngestor();
        schemaValidator = new AEPValidator();
    }

}

import ToolsInterfaces.*;
import Tools.Cataloguer;
import Tools.Ingestor;
import Tools.Validator;
import java.util.List;

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
        dataCataloguer = new Cataloguer();
        dataIngestor = new Ingestor();
        schemaValidator = new Validator();
    }

    public void uploadWithoutSchema(List<String> classIds, List<String> mixinIds, String fileName) {

    }

    public void uploadWithoutDataset(String schemaId, String fileName) {

    }

    public void uploadFile(String datasetId, String fileName) {

    }
}
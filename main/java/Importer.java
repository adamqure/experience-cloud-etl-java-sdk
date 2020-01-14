
import ParameterClasses.Classes.AuthInfo;
import ToolsInterfaces.*;
import Tools.Cataloguer;
import Tools.Ingestor;
import Tools.Validator;
import com.google.gson.Gson;

import java.io.File;
import java.util.List;
import java.util.Scanner;

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
        Gson deserializer = new Gson();
        File config = new File("./config.json");
        AuthInfo authInfo = null;
        try
        {
            String text = new Scanner(config).useDelimiter("\\A").next();
            authInfo = deserializer.fromJson(text, AuthInfo.class);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
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
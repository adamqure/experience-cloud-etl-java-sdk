package java;

import java.AEPParameters.AEPAuthInfo;
import java.AEPTools.AEPCataloguer;
import java.AEPTools.AEPIngestor;
import java.AEPTools.AEPValidator;
import java.ToolInterfaces.*;
import java.io.File;
import java.util.Scanner;

import com.google.gson.*;

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
        Gson  deserializer = new Gson();
        File config = new File("./config.json");
        try
        {
            String text = new Scanner(config).useDelimiter("\\A").next();
            AEPAuthInfo authInfo = deserializer.fromJson(text, AEPAuthInfo.class);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        dataCataloguer = new AEPCataloguer();
        dataIngestor = new AEPIngestor();
        schemaValidator = new AEPValidator();

    }
}
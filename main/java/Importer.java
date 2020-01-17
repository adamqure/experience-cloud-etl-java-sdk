
import ParameterClasses.Abstracts.AuthInfoInterface;
import ParameterClasses.Abstracts.DataSetIdInterface;
import ParameterClasses.Abstracts.SchemaInterface;
import ParameterClasses.Classes.AuthInfo;
import ParameterClasses.Classes.DataSetID;
import ToolsInterfaces.*;
import Tools.Cataloguer;
import Tools.Ingestor;
import Tools.Validator;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Scanner;
import io.jsonwebtoken.*;

/**
 * Basic importer class that implements the ImporterInterface
 * Importer uses an ingestor, validator, and cataloguer
 */
public class Importer
{
    private IngestorInterface dataIngestor;
    private CataloguerInterface dataCataloguer;
    private ValidatorInterface schemaValidator;
    private AuthInfoInterface authInfo;

    public Importer() {
        Gson deserializer = new Gson();
        File config = new File("config.json");
        authInfo = null;
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

    public void Upload(FileInputStream inputStream, SchemaInterface schema, DataSetIdInterface dsId)
    {
        dataIngestor.Upload(inputStream, schema, dsId, authInfo.getAccessToken());
    }

    public void createJwt() {

    }

    private void generateJWT() {

    }
}
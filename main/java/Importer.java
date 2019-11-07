package java;

import java.ToolsInterfaces.*;

public class Importer
{
    private IngestorInterface dataIngestor;
    private CataloguerInterface dataCataloguer;

    public Importer(IngestorInterface ingestor, CataloguerInterface cataloguer)
    {
        dataCataloguer = cataloguer;
        dataIngestor = ingestor;
    }


}
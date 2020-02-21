import Tools.Importer;
import Tools.Ingestor;

import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        String datasetId = "5e29e7e984479018a93e70a7";

//        Ingestor ingestor = new Ingestor();
//        List<String> files = ingestor.splitLargeFile("test500MB.json");
//        System.out.println("FILES GENERATED: " + files.toString());

        Importer importer = new Importer();
//        importer.uploadFileSync("test128.json", null, datasetId);

//        String batchId = importer.createBatch(datasetId);
//        importer.addFileToBatch(batchId, datasetId,"test500MB.json", true);
//        importer.getBatchStatus(batchId);
//        importer.closeBatch(batchId);
//        importer.getBatchStatus(batchId);

        importer.closeBatch("6752b8b0-492a-11ea-886c-7385a96b2ac2");
        importer.getBatchStatus("6752b8b0-492a-11ea-886c-7385a96b2ac2");
    }
}

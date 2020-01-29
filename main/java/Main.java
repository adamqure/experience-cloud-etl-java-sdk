import Tools.Importer;

public class Main {
    public static void main(String[] args) {
        Importer test = new Importer();
        String batchId = test.uploadFile("test128.json", null, "5e29e7e984479018a93e70a7");
        test.getBatchStatus(batchId);
//        test.getBatchStatus("f18fac60-4216-11ea-ada0-b572beddb1e7");
    }
}

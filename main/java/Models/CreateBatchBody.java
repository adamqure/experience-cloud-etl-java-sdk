package Models;

import java.util.HashMap;
import java.util.Map;

public class CreateBatchBody {

    private String datasetId;
    private Map<String, Object> inputFormat;
    public CreateBatchBody(String id)
    {
        inputFormat = new HashMap<>();
        datasetId = id;
        inputFormat.put("format", "json");
        inputFormat.put("isMultilineJson", true);
    }

    public String getDatasetId() {
        return datasetId;
    }

    public Map<String, Object> getInputFormat() {
        return inputFormat;
    }

    public void setDatasetId(String datasetId) {
        this.datasetId = datasetId;
    }

    public void setInputFormat(Map<String, Object> inputFormat) {
        this.inputFormat = inputFormat;
    }

    @Override
    public String toString() {
        return "CreateBatchBody{" +
                "datasetId='" + datasetId + '\'' +
                ", inputFormat=" + inputFormat.toString() +
                '}';
    }
}


package ecom.ble.health.passport.model.record;

import com.fasterxml.jackson.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "analysisDate",
    "aiModel",
    "version"
})

public class Metadata {

    @JsonProperty("analysisDate")
    private String analysisDate;
    @JsonProperty("aiModel")
    private String aiModel;
    @JsonProperty("version")
    private String version;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("analysisDate")
    public String getAnalysisDate() {
        return analysisDate;
    }

    @JsonProperty("analysisDate")
    public void setAnalysisDate(String analysisDate) {
        this.analysisDate = analysisDate;
    }

    @JsonProperty("aiModel")
    public String getAiModel() {
        return aiModel;
    }

    @JsonProperty("aiModel")
    public void setAiModel(String aiModel) {
        this.aiModel = aiModel;
    }

    @JsonProperty("version")
    public String getVersion() {
        return version;
    }

    @JsonProperty("version")
    public void setVersion(String version) {
        this.version = version;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}

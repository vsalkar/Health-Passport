
package ecom.ble.health.passport.model.record;

import com.fasterxml.jackson.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "brief",
    "reportDate",
    "patientInfo"
})

public class Summary {

    @JsonProperty("brief")
    private String brief;
    @JsonProperty("reportDate")
    private String reportDate;
    @JsonProperty("patientInfo")
    private PatientInfo patientInfo;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("brief")
    public String getBrief() {
        return brief;
    }

    @JsonProperty("brief")
    public void setBrief(String brief) {
        this.brief = brief;
    }

    @JsonProperty("reportDate")
    public String getReportDate() {
        return reportDate;
    }

    @JsonProperty("reportDate")
    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    @JsonProperty("patientInfo")
    public PatientInfo getPatientInfo() {
        return patientInfo;
    }

    @JsonProperty("patientInfo")
    public void setPatientInfo(PatientInfo patientInfo) {
        this.patientInfo = patientInfo;
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

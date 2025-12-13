
package ecom.ble.health.passport.model.record;

import com.fasterxml.jackson.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "parameter",
    "yourValue",
    "idealRange",
    "status",
    "severity",
    "severityColor",
    "percentageOutOfRange"
})

public class OutOfRangeValue {

    @JsonProperty("parameter")
    private String parameter;
    @JsonProperty("yourValue")
    private String yourValue;
    @JsonProperty("idealRange")
    private String idealRange;
    @JsonProperty("status")
    private String status;
    @JsonProperty("severity")
    private String severity;
    @JsonProperty("severityColor")
    private String severityColor;
    @JsonProperty("percentageOutOfRange")
    private Double percentageOutOfRange;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("parameter")
    public String getParameter() {
        return parameter;
    }

    @JsonProperty("parameter")
    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    @JsonProperty("yourValue")
    public String getYourValue() {
        return yourValue;
    }

    @JsonProperty("yourValue")
    public void setYourValue(String yourValue) {
        this.yourValue = yourValue;
    }

    @JsonProperty("idealRange")
    public String getIdealRange() {
        return idealRange;
    }

    @JsonProperty("idealRange")
    public void setIdealRange(String idealRange) {
        this.idealRange = idealRange;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("severity")
    public String getSeverity() {
        return severity;
    }

    @JsonProperty("severity")
    public void setSeverity(String severity) {
        this.severity = severity;
    }

    @JsonProperty("severityColor")
    public String getSeverityColor() {
        return severityColor;
    }

    @JsonProperty("severityColor")
    public void setSeverityColor(String severityColor) {
        this.severityColor = severityColor;
    }

    @JsonProperty("percentageOutOfRange")
    public Double getPercentageOutOfRange() {
        return percentageOutOfRange;
    }

    @JsonProperty("percentageOutOfRange")
    public void setPercentageOutOfRange(Double percentageOutOfRange) {
        this.percentageOutOfRange = percentageOutOfRange;
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

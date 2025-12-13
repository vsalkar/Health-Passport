
package ecom.ble.health.passport.model.record;

import com.fasterxml.jackson.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "parameter",
    "value",
    "severity",
    "whyItMatters",
    "potentialCauses",
    "riskLevel",
    "targetValue",
    "immediateAction"
})

public class AreasOfConcern {

    @JsonProperty("parameter")
    private String parameter;
    @JsonProperty("value")
    private String value;
    @JsonProperty("severity")
    private String severity;
    @JsonProperty("whyItMatters")
    private String whyItMatters;
    @JsonProperty("potentialCauses")
    private List<String> potentialCauses;
    @JsonProperty("riskLevel")
    private String riskLevel;
    @JsonProperty("targetValue")
    private String targetValue;
    @JsonProperty("immediateAction")
    private String immediateAction;
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

    @JsonProperty("value")
    public String getValue() {
        return value;
    }

    @JsonProperty("value")
    public void setValue(String value) {
        this.value = value;
    }

    @JsonProperty("severity")
    public String getSeverity() {
        return severity;
    }

    @JsonProperty("severity")
    public void setSeverity(String severity) {
        this.severity = severity;
    }

    @JsonProperty("whyItMatters")
    public String getWhyItMatters() {
        return whyItMatters;
    }

    @JsonProperty("whyItMatters")
    public void setWhyItMatters(String whyItMatters) {
        this.whyItMatters = whyItMatters;
    }

    @JsonProperty("potentialCauses")
    public List<String> getPotentialCauses() {
        return potentialCauses;
    }

    @JsonProperty("potentialCauses")
    public void setPotentialCauses(List<String> potentialCauses) {
        this.potentialCauses = potentialCauses;
    }

    @JsonProperty("riskLevel")
    public String getRiskLevel() {
        return riskLevel;
    }

    @JsonProperty("riskLevel")
    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    @JsonProperty("targetValue")
    public String getTargetValue() {
        return targetValue;
    }

    @JsonProperty("targetValue")
    public void setTargetValue(String targetValue) {
        this.targetValue = targetValue;
    }

    @JsonProperty("immediateAction")
    public String getImmediateAction() {
        return immediateAction;
    }

    @JsonProperty("immediateAction")
    public void setImmediateAction(String immediateAction) {
        this.immediateAction = immediateAction;
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


package ecom.ble.health.passport.model.record;

import com.fasterxml.jackson.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "parameter",
    "value",
    "why",
    "maintain"
})

public class PositiveIndicator {

    @JsonProperty("parameter")
    private String parameter;
    @JsonProperty("value")
    private String value;
    @JsonProperty("why")
    private String why;
    @JsonProperty("maintain")
    private String maintain;
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

    @JsonProperty("why")
    public String getWhy() {
        return why;
    }

    @JsonProperty("why")
    public void setWhy(String why) {
        this.why = why;
    }

    @JsonProperty("maintain")
    public String getMaintain() {
        return maintain;
    }

    @JsonProperty("maintain")
    public void setMaintain(String maintain) {
        this.maintain = maintain;
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

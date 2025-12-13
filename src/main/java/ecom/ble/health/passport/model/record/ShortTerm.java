
package ecom.ble.health.passport.model.record;

import com.fasterxml.jackson.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "timeframe",
    "dietary",
    "lifestyle",
    "supplements"
})

public class ShortTerm {

    @JsonProperty("timeframe")
    private String timeframe;
    @JsonProperty("dietary")
    private List<String> dietary;
    @JsonProperty("lifestyle")
    private List<String> lifestyle;
    @JsonProperty("supplements")
    private List<String> supplements;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("timeframe")
    public String getTimeframe() {
        return timeframe;
    }

    @JsonProperty("timeframe")
    public void setTimeframe(String timeframe) {
        this.timeframe = timeframe;
    }

    @JsonProperty("dietary")
    public List<String> getDietary() {
        return dietary;
    }

    @JsonProperty("dietary")
    public void setDietary(List<String> dietary) {
        this.dietary = dietary;
    }

    @JsonProperty("lifestyle")
    public List<String> getLifestyle() {
        return lifestyle;
    }

    @JsonProperty("lifestyle")
    public void setLifestyle(List<String> lifestyle) {
        this.lifestyle = lifestyle;
    }

    @JsonProperty("supplements")
    public List<String> getSupplements() {
        return supplements;
    }

    @JsonProperty("supplements")
    public void setSupplements(List<String> supplements) {
        this.supplements = supplements;
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

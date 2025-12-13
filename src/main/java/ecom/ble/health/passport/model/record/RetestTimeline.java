
package ecom.ble.health.passport.model.record;

import com.fasterxml.jackson.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "timeframe",
    "tests"
})

public class RetestTimeline {

    @JsonProperty("timeframe")
    private String timeframe;
    @JsonProperty("tests")
    private List<String> tests;
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

    @JsonProperty("tests")
    public List<String> getTests() {
        return tests;
    }

    @JsonProperty("tests")
    public void setTests(List<String> tests) {
        this.tests = tests;
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

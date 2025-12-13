
package ecom.ble.health.passport.model.record;

import com.fasterxml.jackson.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "timeframe",
    "habits",
    "monitoring"
})

public class LongTerm {

    @JsonProperty("timeframe")
    private String timeframe;
    @JsonProperty("habits")
    private List<String> habits;
    @JsonProperty("monitoring")
    private List<String> monitoring;
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

    @JsonProperty("habits")
    public List<String> getHabits() {
        return habits;
    }

    @JsonProperty("habits")
    public void setHabits(List<String> habits) {
        this.habits = habits;
    }

    @JsonProperty("monitoring")
    public List<String> getMonitoring() {
        return monitoring;
    }

    @JsonProperty("monitoring")
    public void setMonitoring(List<String> monitoring) {
        this.monitoring = monitoring;
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

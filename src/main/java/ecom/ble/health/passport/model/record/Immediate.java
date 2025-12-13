
package ecom.ble.health.passport.model.record;

import com.fasterxml.jackson.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "timeframe",
    "actions"
})

public class Immediate {

    @JsonProperty("timeframe")
    private String timeframe;
    @JsonProperty("actions")
    private List<Action> actions;
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

    @JsonProperty("actions")
    public List<Action> getActions() {
        return actions;
    }

    @JsonProperty("actions")
    public void setActions(List<Action> actions) {
        this.actions = actions;
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

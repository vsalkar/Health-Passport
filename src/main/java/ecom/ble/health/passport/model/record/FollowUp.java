
package ecom.ble.health.passport.model.record;

import com.fasterxml.jackson.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "retestTimeline",
    "discussWithDoctor",
    "warningSigns"
})

public class FollowUp {

    @JsonProperty("retestTimeline")
    private List<RetestTimeline> retestTimeline;
    @JsonProperty("discussWithDoctor")
    private List<String> discussWithDoctor;
    @JsonProperty("warningSigns")
    private List<String> warningSigns;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("retestTimeline")
    public List<RetestTimeline> getRetestTimeline() {
        return retestTimeline;
    }

    @JsonProperty("retestTimeline")
    public void setRetestTimeline(List<RetestTimeline> retestTimeline) {
        this.retestTimeline = retestTimeline;
    }

    @JsonProperty("discussWithDoctor")
    public List<String> getDiscussWithDoctor() {
        return discussWithDoctor;
    }

    @JsonProperty("discussWithDoctor")
    public void setDiscussWithDoctor(List<String> discussWithDoctor) {
        this.discussWithDoctor = discussWithDoctor;
    }

    @JsonProperty("warningSigns")
    public List<String> getWarningSigns() {
        return warningSigns;
    }

    @JsonProperty("warningSigns")
    public void setWarningSigns(List<String> warningSigns) {
        this.warningSigns = warningSigns;
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

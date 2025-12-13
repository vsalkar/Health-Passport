
package ecom.ble.health.passport.model.record;

import com.fasterxml.jackson.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "byCategory"
})

public class DetailedAnalysis {

    @JsonProperty("byCategory")
    private List<ByCategory> byCategory;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("byCategory")
    public List<ByCategory> getByCategory() {
        return byCategory;
    }

    @JsonProperty("byCategory")
    public void setByCategory(List<ByCategory> byCategory) {
        this.byCategory = byCategory;
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

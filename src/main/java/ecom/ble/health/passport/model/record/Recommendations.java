
package ecom.ble.health.passport.model.record;

import com.fasterxml.jackson.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "immediate",
    "shortTerm",
    "longTerm"
})

public class Recommendations {

    @JsonProperty("immediate")
    private Immediate immediate;
    @JsonProperty("shortTerm")
    private ShortTerm shortTerm;
    @JsonProperty("longTerm")
    private LongTerm longTerm;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("immediate")
    public Immediate getImmediate() {
        return immediate;
    }

    @JsonProperty("immediate")
    public void setImmediate(Immediate immediate) {
        this.immediate = immediate;
    }

    @JsonProperty("shortTerm")
    public ShortTerm getShortTerm() {
        return shortTerm;
    }

    @JsonProperty("shortTerm")
    public void setShortTerm(ShortTerm shortTerm) {
        this.shortTerm = shortTerm;
    }

    @JsonProperty("longTerm")
    public LongTerm getLongTerm() {
        return longTerm;
    }

    @JsonProperty("longTerm")
    public void setLongTerm(LongTerm longTerm) {
        this.longTerm = longTerm;
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

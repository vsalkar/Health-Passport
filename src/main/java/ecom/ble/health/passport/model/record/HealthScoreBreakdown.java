
package ecom.ble.health.passport.model.record;

import com.fasterxml.jackson.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "cardiovascular",
    "metabolic",
    "organFunction",
    "nutritional"
})

public class HealthScoreBreakdown {

    @JsonProperty("cardiovascular")
    private Cardiovascular cardiovascular;
    @JsonProperty("metabolic")
    private Metabolic metabolic;
    @JsonProperty("organFunction")
    private OrganFunction organFunction;
    @JsonProperty("nutritional")
    private Nutritional nutritional;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("cardiovascular")
    public Cardiovascular getCardiovascular() {
        return cardiovascular;
    }

    @JsonProperty("cardiovascular")
    public void setCardiovascular(Cardiovascular cardiovascular) {
        this.cardiovascular = cardiovascular;
    }

    @JsonProperty("metabolic")
    public Metabolic getMetabolic() {
        return metabolic;
    }

    @JsonProperty("metabolic")
    public void setMetabolic(Metabolic metabolic) {
        this.metabolic = metabolic;
    }

    @JsonProperty("organFunction")
    public OrganFunction getOrganFunction() {
        return organFunction;
    }

    @JsonProperty("organFunction")
    public void setOrganFunction(OrganFunction organFunction) {
        this.organFunction = organFunction;
    }

    @JsonProperty("nutritional")
    public Nutritional getNutritional() {
        return nutritional;
    }

    @JsonProperty("nutritional")
    public void setNutritional(Nutritional nutritional) {
        this.nutritional = nutritional;
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

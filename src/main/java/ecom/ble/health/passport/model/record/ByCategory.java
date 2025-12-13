
package ecom.ble.health.passport.model.record;

import com.fasterxml.jackson.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "category",
    "score",
    "maxScore",
    "findings",
    "keyPoints"
})

public class ByCategory {

    @JsonProperty("category")
    private String category;
    @JsonProperty("score")
    private Integer score;
    @JsonProperty("maxScore")
    private Integer maxScore;
    @JsonProperty("findings")
    private String findings;
    @JsonProperty("keyPoints")
    private List<String> keyPoints;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("category")
    public String getCategory() {
        return category;
    }

    @JsonProperty("category")
    public void setCategory(String category) {
        this.category = category;
    }

    @JsonProperty("score")
    public Integer getScore() {
        return score;
    }

    @JsonProperty("score")
    public void setScore(Integer score) {
        this.score = score;
    }

    @JsonProperty("maxScore")
    public Integer getMaxScore() {
        return maxScore;
    }

    @JsonProperty("maxScore")
    public void setMaxScore(Integer maxScore) {
        this.maxScore = maxScore;
    }

    @JsonProperty("findings")
    public String getFindings() {
        return findings;
    }

    @JsonProperty("findings")
    public void setFindings(String findings) {
        this.findings = findings;
    }

    @JsonProperty("keyPoints")
    public List<String> getKeyPoints() {
        return keyPoints;
    }

    @JsonProperty("keyPoints")
    public void setKeyPoints(List<String> keyPoints) {
        this.keyPoints = keyPoints;
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

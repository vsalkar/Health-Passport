
package ecom.ble.health.passport.model.record;

import com.fasterxml.jackson.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "score",
    "maxScore",
    "rating",
    "ratingColor",
    "assessment"
})

public class OverallHealthScore {

    @JsonProperty("score")
    private Integer score;
    @JsonProperty("maxScore")
    private Integer maxScore;
    @JsonProperty("rating")
    private String rating;
    @JsonProperty("ratingColor")
    private String ratingColor;
    @JsonProperty("assessment")
    private String assessment;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

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

    @JsonProperty("rating")
    public String getRating() {
        return rating;
    }

    @JsonProperty("rating")
    public void setRating(String rating) {
        this.rating = rating;
    }

    @JsonProperty("ratingColor")
    public String getRatingColor() {
        return ratingColor;
    }

    @JsonProperty("ratingColor")
    public void setRatingColor(String ratingColor) {
        this.ratingColor = ratingColor;
    }

    @JsonProperty("assessment")
    public String getAssessment() {
        return assessment;
    }

    @JsonProperty("assessment")
    public void setAssessment(String assessment) {
        this.assessment = assessment;
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

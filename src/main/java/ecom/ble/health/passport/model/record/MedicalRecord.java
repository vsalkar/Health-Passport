
package ecom.ble.health.passport.model.record;

import com.fasterxml.jackson.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)

public class MedicalRecord {
    @JsonProperty("overallHealthScore")
    private OverallHealthScore overallHealthScore;
    @JsonProperty("recordId")
    private String recordId;
    @JsonProperty("outOfRangeValues")
    private List<OutOfRangeValue> outOfRangeValues;
    @JsonProperty("summary")
    private Summary summary;
    @JsonProperty("keyFindings")
    private KeyFindings keyFindings;
    @JsonProperty("detailedAnalysis")
    private DetailedAnalysis detailedAnalysis;
    @JsonProperty("areasOfConcern")
    private List<AreasOfConcern> areasOfConcern;
    @JsonProperty("positiveIndicators")
    private List<PositiveIndicator> positiveIndicators;
    @JsonProperty("recommendations")
    private Recommendations recommendations;
    @JsonProperty("followUp")
    private FollowUp followUp;
    @JsonProperty("healthScoreBreakdown")
    private HealthScoreBreakdown healthScoreBreakdown;
    @JsonProperty("metadata")
    private Metadata metadata;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("overallHealthScore")
    public OverallHealthScore getOverallHealthScore() {
        return overallHealthScore;
    }

    @JsonProperty("overallHealthScore")
    public void setOverallHealthScore(OverallHealthScore overallHealthScore) {
        this.overallHealthScore = overallHealthScore;
    }

    @JsonProperty("outOfRangeValues")
    public List<OutOfRangeValue> getOutOfRangeValues() {
        return outOfRangeValues;
    }

    @JsonProperty("outOfRangeValues")
    public void setOutOfRangeValues(List<OutOfRangeValue> outOfRangeValues) {
        this.outOfRangeValues = outOfRangeValues;
    }

    @JsonProperty("summary")
    public Summary getSummary() {
        return summary;
    }

    @JsonProperty("summary")
    public void setSummary(Summary summary) {
        this.summary = summary;
    }

    @JsonProperty("keyFindings")
    public KeyFindings getKeyFindings() {
        return keyFindings;
    }

    @JsonProperty("keyFindings")
    public void setKeyFindings(KeyFindings keyFindings) {
        this.keyFindings = keyFindings;
    }

    @JsonProperty("detailedAnalysis")
    public DetailedAnalysis getDetailedAnalysis() {
        return detailedAnalysis;
    }

    @JsonProperty("detailedAnalysis")
    public void setDetailedAnalysis(DetailedAnalysis detailedAnalysis) {
        this.detailedAnalysis = detailedAnalysis;
    }

    @JsonProperty("areasOfConcern")
    public List<AreasOfConcern> getAreasOfConcern() {
        return areasOfConcern;
    }

    @JsonProperty("areasOfConcern")
    public void setAreasOfConcern(List<AreasOfConcern> areasOfConcern) {
        this.areasOfConcern = areasOfConcern;
    }

    @JsonProperty("positiveIndicators")
    public List<PositiveIndicator> getPositiveIndicators() {
        return positiveIndicators;
    }

    @JsonProperty("positiveIndicators")
    public void setPositiveIndicators(List<PositiveIndicator> positiveIndicators) {
        this.positiveIndicators = positiveIndicators;
    }

    @JsonProperty("recommendations")
    public Recommendations getRecommendations() {
        return recommendations;
    }

    @JsonProperty("recommendations")
    public void setRecommendations(Recommendations recommendations) {
        this.recommendations = recommendations;
    }

    @JsonProperty("followUp")
    public FollowUp getFollowUp() {
        return followUp;
    }

    @JsonProperty("followUp")
    public void setFollowUp(FollowUp followUp) {
        this.followUp = followUp;
    }

    @JsonProperty("healthScoreBreakdown")
    public HealthScoreBreakdown getHealthScoreBreakdown() {
        return healthScoreBreakdown;
    }

    @JsonProperty("healthScoreBreakdown")
    public void setHealthScoreBreakdown(HealthScoreBreakdown healthScoreBreakdown) {
        this.healthScoreBreakdown = healthScoreBreakdown;
    }

    @JsonProperty("metadata")
    public Metadata getMetadata() {
        return metadata;
    }

    @JsonProperty("metadata")
    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @JsonAnyGetter
    public String getRecordId() {
        return recordId;
    }
    @JsonAnySetter
    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }
}

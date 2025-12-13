package ecom.ble.health.passport.analyzer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ecom.ble.health.passport.ai.OpenAiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class HealthReportAnalyzerService {

    private static final Pattern JSON_CODE_BLOCK = Pattern.compile("```json\\s*(.*?)\\s*```", Pattern.DOTALL);

    private final StoredFileReader storedFileReader;
    private final OpenAiClient openAiClient;
    private final ObjectMapper objectMapper;

    @Value("${openai.model:gpt-4o}")
    private String defaultModel;

    public JsonNode analyze(String userId, String filename, AnalyzerRequest request) {
        FileContent file = storedFileReader.readForHealthReport(userId, filename);
        String model = (request != null && request.model() != null && !request.model().isBlank())
                ? request.model()
                : defaultModel;

        String prompt = (request != null && request.customPrompt() != null && !request.customPrompt().isBlank())
                ? request.customPrompt()
                : defaultPrompt();

        ObjectNode body = objectMapper.createObjectNode();
        body.put("model", model);
        body.put("max_tokens", 4000);
        body.put("temperature", 0.2);
        body.set("response_format", objectMapper.createObjectNode().put("type", "json_object"));

        var messages = objectMapper.createArrayNode();
        messages.add(objectMapper.createObjectNode()
                .put("role", "system")
                .put("content", "You are an expert medical AI assistant. You MUST respond with valid JSON only, no markdown formatting."));

        if ("text".equals(file.type())) {
            messages.add(objectMapper.createObjectNode()
                    .put("role", "user")
                    .put("content", prompt + "\n\nHealth Report:\n" + (file.textContent() == null ? "" : file.textContent())));
        } else if ("image".equals(file.type())) {
            var user = objectMapper.createObjectNode();
            user.put("role", "user");
            var content = objectMapper.createArrayNode();
            content.add(objectMapper.createObjectNode().put("type", "text").put("text", prompt));
            content.add(objectMapper.createObjectNode()
                    .put("type", "image_url")
                    .set("image_url", objectMapper.createObjectNode()
                            .put("url", "data:" + file.mimeType() + ";base64," + file.base64Content())));
            user.set("content", content);
            messages.add(user);
        } else {
            throw new IllegalArgumentException("Unsupported content type for health report: " + file.type());
        }

        body.set("messages", messages);

        JsonNode response = openAiClient.createChatCompletion(body);
        String content = openAiClient.extractFirstMessageContent(response);

        if (content == null) {
            return objectMapper.createObjectNode()
                    .put("error", "Empty response from OpenAI")
                    .set("rawResponse", response);
        }

        // Parse JSON (forced by response_format, but keep safety net like Python implementation)
        try {
            return objectMapper.readTree(content);
        } catch (Exception ignored) {
            Matcher matcher = JSON_CODE_BLOCK.matcher(content);
            if (matcher.find()) {
                try {
                    return objectMapper.readTree(matcher.group(1));
                } catch (Exception ignored2) {
                    // fall through
                }
            }
            return objectMapper.createObjectNode()
                    .put("error", "Failed to parse JSON response")
                    .put("rawResponse", content);
        }
    }

    private String defaultPrompt() {
        return """
        You are a medical AI assistant analyzing a health report.
        
        CRITICAL: You MUST respond with ONLY valid JSON. No markdown, no code blocks, no extra text.
        
        Analyze the health report and return a JSON object with this EXACT structure:
        
        {
          "overallHealthScore": {
            "score": 75,
            "maxScore": 100,
            "rating": "Good",
            "ratingColor": "green",
            "assessment": "Brief one-line assessment"
          },
          "outOfRangeValues": [
            {
              "parameter": "LDL Cholesterol",
              "yourValue": "140 mg/dL",
              "idealRange": "<100 mg/dL",
              "status": "High",
              "severity": "Moderate",
              "severityColor": "orange",
              "percentageOutOfRange": 40
            }
          ],
          "summary": {
            "brief": "2-3 sentence overview",
            "reportDate": "2024-12-13",
            "patientInfo": {
              "name": "If available",
              "age": "If available",
              "gender": "If available"
            }
          },
          "keyFindings": {
            "categories": [
              {
                "name": "Complete Blood Count",
                "tests": [
                  {
                    "name": "Hemoglobin",
                    "value": "14.5 g/dL",
                    "normalRange": "13.5-17.5 g/dL",
                    "status": "normal",
                    "icon": "✓"
                  }
                ]
              }
            ]
          },
          "detailedAnalysis": {
            "byCategory": [
              {
                "category": "Cardiovascular Health",
                "score": 60,
                "maxScore": 100,
                "findings": "Detailed explanation",
                "keyPoints": ["Point 1", "Point 2"]
              }
            ]
          },
          "areasOfConcern": [
            {
              "parameter": "LDL Cholesterol",
              "value": "140 mg/dL",
              "severity": "Moderate",
              "whyItMatters": "Explanation",
              "potentialCauses": ["Cause 1", "Cause 2"],
              "riskLevel": "Medium",
              "targetValue": "<100 mg/dL",
              "immediateAction": "Specific action"
            }
          ],
          "positiveIndicators": [
            {
              "parameter": "Kidney Function",
              "value": "Normal",
              "why": "Good kidney filtration",
              "maintain": "Keep current habits"
            }
          ],
          "recommendations": {
            "immediate": {
              "timeframe": "1-2 weeks",
              "actions": [
                {
                  "action": "Start Vitamin D supplementation",
                  "details": "2000-4000 IU daily",
                  "priority": "High"
                }
              ]
            },
            "shortTerm": {
              "timeframe": "1-3 months",
              "dietary": ["Change 1", "Change 2"],
              "lifestyle": ["Change 1", "Change 2"],
              "supplements": ["Supplement 1"]
            },
            "longTerm": {
              "timeframe": "3-6 months",
              "habits": ["Habit 1", "Habit 2"],
              "monitoring": ["Monitor 1", "Monitor 2"]
            }
          },
          "followUp": {
            "retestTimeline": [
              {
                "timeframe": "3 months",
                "tests": ["Lipid panel", "Fasting glucose"]
              }
            ],
            "discussWithDoctor": ["Topic 1", "Topic 2"],
            "warningSigns": ["Sign 1", "Sign 2"]
          },
          "healthScoreBreakdown": {
            "cardiovascular": {
              "score": 15,
              "maxScore": 25,
              "status": "Needs Improvement"
            },
            "metabolic": {
              "score": 16,
              "maxScore": 25,
              "status": "Needs Improvement"
            },
            "organFunction": {
              "score": 21,
              "maxScore": 25,
              "status": "Good"
            },
            "nutritional": {
              "score": 16,
              "maxScore": 25,
              "status": "Fair"
            }
          },
          "metadata": {
            "analysisDate": "2024-12-13T10:30:00Z",
            "aiModel": "gpt-4o",
            "version": "1.0"
          }
        }
        
        IMPORTANT RULES:
        1. Return ONLY the JSON object, nothing else
        2. All numeric scores should be numbers, not strings
        3. Include all fields even if data is not available (use null or "Not available")
        4. Ensure JSON is properly formatted and valid
        5. Use severity colors: "red" (critical), "orange" (moderate), "yellow" (mild), "green" (normal)
        6. Calculate health scores based on available data
        7. Be thorough but accurate
        
        This is for informational purposes only and not medical advice.
        """;
    }
}



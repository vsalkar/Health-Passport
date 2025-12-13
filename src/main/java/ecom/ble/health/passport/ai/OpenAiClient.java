package ecom.ble.health.passport.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

/**
 * Minimal OpenAI Chat Completions client.
 *
 * Uses RestClient (spring-web) to avoid relying on Spring AI autoconfiguration.
 */
@Component
@RequiredArgsConstructor
public class OpenAiClient {

    private final ObjectMapper objectMapper;

    private final RestClient restClient = RestClient.builder().build();

    @Value("${openai.api-key:}")
    private String apiKey;

    @Value("${openai.base-url:https://api.openai.com/v1}")
    private String baseUrl;

    public JsonNode createChatCompletion(JsonNode requestBody) {
        String resolvedApiKey = (apiKey == null || apiKey.isBlank()) ? System.getenv("OPENAI_API_KEY") : apiKey;
        if (resolvedApiKey == null || resolvedApiKey.isBlank()) {
            throw new IllegalStateException("OpenAI API key not configured. Set openai.api-key or env OPENAI_API_KEY.");
        }

        try {
            return restClient
                    .post()
                    .uri(baseUrl + "/chat/completions")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + resolvedApiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .body(requestBody)
                    .retrieve()
                    .body(JsonNode.class);
        } catch (RestClientResponseException ex) {
            // Include upstream body for debugging without leaking secrets.
            String upstreamBody = ex.getResponseBodyAsString();
            throw new OpenAiException(
                    "OpenAI API error: HTTP " + ex.getRawStatusCode() + " - " + ex.getStatusText()
                            + (upstreamBody == null || upstreamBody.isBlank() ? "" : " - " + upstreamBody),
                    ex
            );
        } catch (Exception ex) {
            throw new OpenAiException("OpenAI API call failed: " + ex.getMessage(), ex);
        }
    }

    /**
     * Helper to extract the assistant message content: choices[0].message.content
     */
    public String extractFirstMessageContent(JsonNode responseJson) {
        if (responseJson == null) {
            return null;
        }
        JsonNode contentNode = responseJson.path("choices").path(0).path("message").path("content");
        return contentNode.isMissingNode() || contentNode.isNull() ? null : contentNode.asText();
    }
}



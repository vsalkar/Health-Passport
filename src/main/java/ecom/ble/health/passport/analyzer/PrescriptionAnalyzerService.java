package ecom.ble.health.passport.analyzer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ecom.ble.health.passport.ai.OpenAiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrescriptionAnalyzerService {

    private final StoredFileReader storedFileReader;
    private final OpenAiClient openAiClient;
    private final ObjectMapper objectMapper;

    @Value("${openai.model:gpt-4o}")
    private String defaultModel;

    public String analyze(String userId, String filename, AnalyzerRequest request) {
        FileContent file = storedFileReader.readForPrescription(userId, filename);
        String model = (request != null && request.model() != null && !request.model().isBlank())
                ? request.model()
                : defaultModel;

        String prompt = (request != null && request.customPrompt() != null && !request.customPrompt().isBlank())
                ? request.customPrompt()
                : defaultPrompt();

        ObjectNode body = objectMapper.createObjectNode();
        body.put("model", model);
        body.put("max_tokens", 2500);
        body.put("temperature", 0.2);

        var messages = objectMapper.createArrayNode();
        messages.add(objectMapper.createObjectNode()
                .put("role", "system")
                .put("content",
                        "You are an expert medical AI assistant specializing in prescription analysis and medical document interpretation. " +
                                "You excel at reading handwritten and printed prescriptions accurately."));

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

        body.set("messages", messages);

        JsonNode response = openAiClient.createChatCompletion(body);
        String analysis = openAiClient.extractFirstMessageContent(response);
        return analysis == null ? "" : analysis;
    }

    private String defaultPrompt() {
        return """
        You are a medical AI assistant analyzing a prescription image. Please extract and provide:
        
        **📋 PRESCRIPTION DETAILS**
        
        1. **Patient Information**:
           - Patient name
           - Age/Date of birth (if visible)
           - Patient ID (if visible)
        
        2. **Doctor Information**:
           - Doctor's name
           - Medical registration number (if visible)
           - Hospital/Clinic name
           - Contact information (if visible)
        
        3. **Prescription Date**: Date when prescription was issued
        
        4. **Medications Prescribed** (for each medicine):
           - 💊 **Medicine Name**: [Generic and Brand name if visible]
           - 📊 **Dosage**: [Strength, e.g., 500mg, 10mg]
           - ⏰ **Frequency**: [How often to take - e.g., twice daily, three times daily]
           - 🍽️ **Timing**: [Before/After meals, morning/evening]
           - 📅 **Duration**: [How many days/weeks]
           - 📝 **Instructions**: [Any special instructions]
        
        5. **Diagnosis/Condition** (if mentioned): What condition is being treated
        
        6. **Medical Tests Recommended** (if any): Lab tests or scans prescribed
        
        7. **Follow-up Instructions**:
           - When to return for follow-up
           - Warning signs to watch for
           - Emergency instructions
        
        8. **Important Warnings** ⚠️:
           - Drug interactions to be aware of
           - Common side effects of prescribed medications
           - Contraindications (what to avoid)
           - Allergies or precautions mentioned
        
        9. **Pharmacy Instructions**:
           - Total number of medicines prescribed
           - Any specific pharmacy notes
        
        10. **Additional Notes**: Any other relevant information from the prescription
        
        **FORMAT GUIDELINES:**
        - Use clear headings and bullet points
        - Mark important warnings with ⚠️
        - Use emojis for better readability
        - If any information is unclear or illegible, mention it
        - If handwriting is difficult to read, provide best interpretation with [uncertain] tag
        
        **IMPORTANT DISCLAIMERS:**
        - This is for informational and record-keeping purposes only
        - Always follow your doctor's instructions exactly as prescribed
        - Consult your pharmacist or doctor if you have questions
        - Do not self-medicate or alter dosages without medical advice
        - Keep original prescription for pharmacy and legal purposes
        
        Please be thorough and accurate in extracting all visible information from the prescription.
        """;
    }
}



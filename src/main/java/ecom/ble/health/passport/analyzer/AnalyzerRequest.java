package ecom.ble.health.passport.analyzer;

/**
 * Optional request body to override default prompts / model.
 */
public record AnalyzerRequest(
        String customPrompt,
        String model
)
{}



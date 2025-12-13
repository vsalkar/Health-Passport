package ecom.ble.health.passport.analyzer;

/**
 * Normalized representation of a stored file for analysis.
 *
 * @param type "text" or "image"
 * @param textContent extracted text for type=text
 * @param base64Content base64 bytes for type=image (no data: prefix)
 * @param mimeType MIME type for type=image
 * @param source source label (pdf/image/text)
 * @param filename original filename
 */
public record FileContent(
        String type,
        String textContent,
        String base64Content,
        String mimeType,
        String source,
        String filename
)
{}



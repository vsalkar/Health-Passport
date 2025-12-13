package ecom.ble.health.passport.analyzer;

import ecom.ble.health.passport.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.net.URLConnection;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class StoredFileReader {

    private final FileStorageService fileStorageService;
    private final Tika tika = new Tika();

    public FileContent readForHealthReport(String userId, String filename) {
        Resource resource = fileStorageService.loadFileAsResource(userId, filename);
        String lower = filename == null ? "" : filename.toLowerCase();

        if (isImage(lower)) {
            return readImage(resource, filename);
        }

        // pdf / doc / docx / txt -> extract text
        return readTextWithTika(resource, filename, lower.endsWith(".pdf") ? "pdf" : "text");
    }

    public FileContent readForPrescription(String userId, String filename) {
        Resource resource = fileStorageService.loadFileAsResource(userId, filename);
        String lower = filename == null ? "" : filename.toLowerCase();
        if (!isImage(lower)) {
            throw new IllegalArgumentException("Prescription analyzer supports images only (.jpg/.jpeg/.png/.gif/.webp).");
        }
        return readImage(resource, filename);
    }

    private FileContent readTextWithTika(Resource resource, String filename, String source) {
        try (InputStream in = resource.getInputStream()) {
            String text = tika.parseToString(in);
            return new FileContent("text", text, null, null, source, filename);
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to read text from file: " + filename + " - " + ex.getMessage(), ex);
        }
    }

    private FileContent readImage(Resource resource, String filename) {
        try (InputStream in = resource.getInputStream()) {
            byte[] bytes = in.readAllBytes();
            String base64 = Base64.getEncoder().encodeToString(bytes);
            String mime = guessMimeType(filename);
            return new FileContent("image", null, base64, mime, "image", filename);
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to read image file: " + filename + " - " + ex.getMessage(), ex);
        }
    }

    private boolean isImage(String lowerFilename) {
        return lowerFilename.endsWith(".jpg")
                || lowerFilename.endsWith(".jpeg")
                || lowerFilename.endsWith(".png")
                || lowerFilename.endsWith(".gif")
                || lowerFilename.endsWith(".webp");
    }

    private String guessMimeType(String filename) {
        String mime = URLConnection.guessContentTypeFromName(filename);
        return (mime == null || mime.isBlank()) ? "image/jpeg" : mime;
    }
}



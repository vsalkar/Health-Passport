package ecom.ble.health.passport.analyzer;

import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

/**
 * Service for removing Protected Health Information (PHI) from text content.
 * Removes patient names, dates of birth, SSNs, addresses, email addresses,
 * phone numbers, and medical record numbers.
 */
@Service
public class PhiRemovalService {

    // Pattern for SSN: XXX-XX-XXXX or XXXXXXXXX
    private static final Pattern SSN_PATTERN = Pattern.compile(
            "\\b\\d{3}-?\\d{2}-?\\d{4}\\b"
    );

    // Pattern for email addresses
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b"
    );

    // Pattern for phone numbers: various formats
    // Matches: (XXX) XXX-XXXX, XXX-XXX-XXXX, XXX.XXX.XXXX, XXX XXX XXXX, XXXXXXXXXX
    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "\\b(?:\\(?\\d{3}\\)?[-.\\s]?)?\\d{3}[-.\\s]?\\d{4}\\b"
    );

    // Pattern for dates of birth: MM/DD/YYYY, DD/MM/YYYY, YYYY-MM-DD, MM-DD-YYYY, etc.
    // Also matches: DOB, Date of Birth, Birth Date, etc. followed by dates
    private static final Pattern DOB_PATTERN = Pattern.compile(
            "(?i)(?:dob|date\\s+of\\s+birth|birth\\s+date|born|birthday)[:\\s]*" +
            "(?:\\d{1,2}[/-]\\d{1,2}[/-]\\d{2,4}|\\d{4}[/-]\\d{1,2}[/-]\\d{1,2})" +
            "|\\b\\d{1,2}[/-]\\d{1,2}[/-]\\d{2,4}\\b|\\b\\d{4}[/-]\\d{1,2}[/-]\\d{1,2}\\b"
    );

    // Pattern for addresses: street addresses with numbers, city/state/zip
    // Matches patterns like: "123 Main St", "123 Main Street", "City, State ZIP"
    private static final Pattern ADDRESS_PATTERN = Pattern.compile(
            "\\b\\d+\\s+[A-Za-z0-9\\s]+(?:Street|St|Avenue|Ave|Road|Rd|Drive|Dr|Lane|Ln|Boulevard|Blvd|Court|Ct|Place|Pl|Way|Circle|Cir)[,\\s]*" +
            "(?:[A-Za-z\\s]+,\\s*)?[A-Z]{2}\\s+\\d{5}(?:-\\d{4})?\\b",
            Pattern.CASE_INSENSITIVE
    );

    // Pattern for medical record numbers: MRN, Medical Record #, Patient ID, etc.
    private static final Pattern MRN_PATTERN = Pattern.compile(
            "(?i)(?:mrn|medical\\s+record\\s+(?:number|#|no\\.?)|patient\\s+id|record\\s+number|account\\s+number)" +
            "[:\\s]*[A-Z0-9-]+",
            Pattern.CASE_INSENSITIVE
    );

    // Pattern for patient names: Common patterns like "Patient:", "Name:", "Patient Name:", etc.
    // This is more challenging - we'll look for common label patterns followed by capitalized words
    private static final Pattern NAME_PATTERN = Pattern.compile(
            "(?i)(?:patient(?:\\s+name)?|name|full\\s+name|patient\\s+id)[:\\s]+" +
            "([A-Z][a-z]+(?:\\s+[A-Z][a-z]+)*)",
            Pattern.CASE_INSENSITIVE
    );

    // Pattern for standalone names (capitalized words that look like names)
    // This is more aggressive and may catch some false positives, but better safe than sorry
    // Matches: "First Last" or "First Middle Last" patterns
    private static final Pattern STANDALONE_NAME_PATTERN = Pattern.compile(
            "\\b([A-Z][a-z]+\\s+[A-Z][a-z]+(?:\\s+[A-Z][a-z]+)?)\\b"
    );

    /**
     * Removes all PHI from the given text content.
     * 
     * @param text The text content to sanitize
     * @return The sanitized text with PHI removed
     */
    public String removePhi(String text) {
        if (text == null || text.isBlank()) {
            return text;
        }

        String sanitized = text;

        // Remove SSNs
        sanitized = SSN_PATTERN.matcher(sanitized).replaceAll("");

        // Remove email addresses
        sanitized = EMAIL_PATTERN.matcher(sanitized).replaceAll("");

        // Remove phone numbers
        sanitized = PHONE_PATTERN.matcher(sanitized).replaceAll("");

        // Remove dates of birth
        sanitized = DOB_PATTERN.matcher(sanitized).replaceAll("");

        // Remove addresses
        sanitized = ADDRESS_PATTERN.matcher(sanitized).replaceAll("");

        // Remove medical record numbers
        sanitized = MRN_PATTERN.matcher(sanitized).replaceAll("");

        // Remove names with labels (e.g., "Patient: John Doe")
        sanitized = NAME_PATTERN.matcher(sanitized).replaceAll("");

        // Remove standalone name patterns (more aggressive - may have false positives)
        // Only remove if it looks like a full name (2-3 capitalized words)
        sanitized = STANDALONE_NAME_PATTERN.matcher(sanitized).replaceAll("");

        // Clean up multiple consecutive spaces and newlines
        sanitized = sanitized.replaceAll("\\s+", " ");
        sanitized = sanitized.replaceAll("\\n\\s*\\n", "\n");

        return sanitized.trim();
    }
}


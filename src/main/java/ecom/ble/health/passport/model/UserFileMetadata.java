package ecom.ble.health.passport.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserFileMetadata {
    private Long fileId;
    private String userId;
    private String originalFileName;
    private String storedFileName;
    private String storagePath;
    private String contentType;
    private Long fileSizeBytes;
    private String checksumSha256;
    private boolean isProcessed;
    private Instant processedAt;
    private Instant uploadedAt;
    private boolean isDeleted;
    private Instant deletedAt;
}



package ecom.ble.health.passport.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadResponse {
    private String fileName;
    private String fileType;
    private long size;
    private String downloadUrl;
    private String message;
    private boolean success;
}


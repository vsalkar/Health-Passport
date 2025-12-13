package ecom.ble.health.passport.controller.impl;

import ecom.ble.health.passport.controller.FileController;
import ecom.ble.health.passport.model.FileUploadResponse;
import ecom.ble.health.passport.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLConnection;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class DefaultFileController implements FileController {

    private final FileStorageService fileStorageService;

    @Override
    public ResponseEntity<List<FileUploadResponse>> uploadFiles(MultipartFile[] files) {
        if (files == null || files.length == 0) {
            return ResponseEntity.badRequest().build();
        }

        List<FileUploadResponse> responses = fileStorageService.storeFiles(files);
        return ResponseEntity.ok(responses);
    }

    @Override
    public ResponseEntity<Resource> downloadFile(String filename) {
        Resource resource = fileStorageService.loadFileAsResource(filename);

        String contentType = URLConnection.guessContentTypeFromName(resource.getFilename());
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @Override
    public ResponseEntity<List<String>> listAllFiles() {
        List<String> files = fileStorageService.listAllFiles();
        return ResponseEntity.ok(files);
    }

    @Override
    public ResponseEntity<String> deleteFile(String filename) {
        boolean deleted = fileStorageService.deleteFile(filename);
        if (deleted) {
            return ResponseEntity.ok("File deleted successfully: " + filename);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found: " + filename);
        }
    }
}


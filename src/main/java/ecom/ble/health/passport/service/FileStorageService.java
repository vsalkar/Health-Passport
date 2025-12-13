package ecom.ble.health.passport.service;

import ecom.ble.health.passport.exception.FileStorageException;
import ecom.ble.health.passport.entity.UserFileEntity;
import ecom.ble.health.passport.model.FileUploadResponse;
import ecom.ble.health.passport.model.UserFileMetadata;
import ecom.ble.health.passport.repository.UserFileRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class FileStorageService {

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    private Path baseStorageLocation;

    private final UserFileRepository userFileRepository;

    @PostConstruct
    public void init() {
        this.baseStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.baseStorageLocation);
        } catch (IOException ex) {
            throw new FileStorageException("Could not create the directory for file uploads.", ex);
        }
    }

    private Path getUserStorageLocation(String userId) {
        if (userId == null || userId.isBlank()) {
            throw new FileStorageException("User ID cannot be null or empty");
        }
        
        // Sanitize userId to prevent directory traversal
        String sanitizedUserId = userId.replaceAll("[^a-zA-Z0-9_-]", "_");
        Path userPath = this.baseStorageLocation.resolve(sanitizedUserId).normalize();
        
        try {
            Files.createDirectories(userPath);
        } catch (IOException ex) {
            throw new FileStorageException("Could not create user directory for: " + userId, ex);
        }
        
        return userPath;
    }

    public List<FileUploadResponse> storeFiles(String userId, MultipartFile[] files) {
        List<FileUploadResponse> responses = new ArrayList<>();

        for (MultipartFile file : files) {
            responses.add(storeFile(userId, file));
        }

        return responses;
    }

    public FileUploadResponse storeFile(String userId, MultipartFile file) {
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        Path userStorageLocation = getUserStorageLocation(userId);

        try {
            if (originalFileName.contains("..")) {
                throw new FileStorageException("Filename contains invalid path sequence: " + originalFileName);
            }

            Path targetLocation = userStorageLocation.resolve(originalFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Persist / upsert metadata in user_files table
            String storagePath = this.baseStorageLocation.relativize(targetLocation).toString().replace('\\', '/');
            UserFileEntity entity = userFileRepository
                    .findFirstByUserIdAndStoredFileName(userId, originalFileName)
                    .orElseGet(UserFileEntity::new);

            entity.setUserId(userId);
            entity.setOriginalFileName(originalFileName);
            entity.setStoredFileName(originalFileName); // we do NOT rename files
            entity.setStoragePath(storagePath);
            entity.setContentType(file.getContentType());
            entity.setFileSizeBytes(file.getSize());
            entity.setDeleted(false);
            entity.setDeletedAt(null);
            // If re-uploading same file name, treat as fresh upload
            entity.setUploadedAt(Instant.now());

            userFileRepository.save(entity);

            String downloadUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/users/")
                    .path(userId)
                    .path("/files/download/")
                    .path(originalFileName)
                    .toUriString();

            return FileUploadResponse.builder()
                    .fileName(originalFileName)
                    .fileType(file.getContentType())
                    .size(file.getSize())
                    .downloadUrl(downloadUrl)
                    .message("File uploaded successfully")
                    .success(true)
                    .build();

        } catch (IOException ex) {
            return FileUploadResponse.builder()
                    .fileName(originalFileName)
                    .message("Failed to upload file: " + ex.getMessage())
                    .success(false)
                    .build();
        }
    }

    public Resource loadFileAsResource(String userId, String fileName) {
        UserFileEntity entity = userFileRepository
                .findFirstByUserIdAndStoredFileNameAndIsDeletedFalse(userId, fileName)
                .orElseThrow(() -> new FileStorageException("File not found: " + fileName));

        Path filePath = this.baseStorageLocation.resolve(Paths.get(entity.getStoragePath())).normalize();

        // Security check: ensure the file is within base uploads directory
        if (!filePath.startsWith(this.baseStorageLocation)) {
            throw new FileStorageException("Access denied to file: " + fileName);
        }
        
        try {
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new FileStorageException("File not found: " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new FileStorageException("File not found: " + fileName, ex);
        }
    }

    public List<UserFileMetadata> listAllFiles(String userId) {
        return userFileRepository.findByUserIdAndIsDeletedFalseOrderByUploadedAtDesc(userId)
                .stream()
                .map(this::toMetadata)
                .toList();
    }

    public boolean deleteFile(String userId, String fileName) {
        UserFileEntity entity = userFileRepository
                .findFirstByUserIdAndStoredFileNameAndIsDeletedFalse(userId, fileName)
                .orElse(null);

        if (entity == null) {
            return false;
        }

        Path filePath = this.baseStorageLocation.resolve(Paths.get(entity.getStoragePath())).normalize();

        // Security check: ensure the file is within base uploads directory
        if (!filePath.startsWith(this.baseStorageLocation)) {
            throw new FileStorageException("Access denied to file: " + fileName);
        }
        
        try {
            Files.deleteIfExists(filePath);

            entity.setDeleted(true);
            entity.setDeletedAt(Instant.now());
            userFileRepository.save(entity);

            return true;
        } catch (IOException ex) {
            throw new FileStorageException("Could not delete file: " + fileName, ex);
        }
    }

    private UserFileMetadata toMetadata(UserFileEntity entity) {
        return UserFileMetadata.builder()
                .fileId(entity.getFileId())
                .userId(entity.getUserId())
                .originalFileName(entity.getOriginalFileName())
                .storedFileName(entity.getStoredFileName())
                .storagePath(entity.getStoragePath())
                .contentType(entity.getContentType())
                .fileSizeBytes(entity.getFileSizeBytes())
                .checksumSha256(entity.getChecksumSha256())
                .isProcessed(entity.isProcessed())
                .processedAt(entity.getProcessedAt())
                .uploadedAt(entity.getUploadedAt())
                .isDeleted(entity.isDeleted())
                .deletedAt(entity.getDeletedAt())
                .build();
    }

}

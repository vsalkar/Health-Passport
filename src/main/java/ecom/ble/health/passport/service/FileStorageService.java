package ecom.ble.health.passport.service;

import ecom.ble.health.passport.exception.FileStorageException;
import ecom.ble.health.passport.model.FileUploadResponse;
import jakarta.annotation.PostConstruct;
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
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@Service
public class FileStorageService {

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    private Path baseStorageLocation;

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
        Path userStorageLocation = getUserStorageLocation(userId);
        
        try {
            Path filePath = userStorageLocation.resolve(fileName).normalize();
            
            // Security check: ensure the file is within user's directory
            if (!filePath.startsWith(userStorageLocation)) {
                throw new FileStorageException("Access denied to file: " + fileName);
            }
            
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

    public List<String> listAllFiles(String userId) {
        Path userStorageLocation = getUserStorageLocation(userId);
        
        if (!Files.exists(userStorageLocation)) {
            return Collections.emptyList();
        }
        
        try (Stream<Path> paths = Files.walk(userStorageLocation, 1)) {
            return paths
                    .filter(Files::isRegularFile)
                    .map(path -> path.getFileName().toString())
                    .toList();
        } catch (IOException ex) {
            throw new FileStorageException("Could not list files for user: " + userId, ex);
        }
    }

    public boolean deleteFile(String userId, String fileName) {
        Path userStorageLocation = getUserStorageLocation(userId);
        
        try {
            Path filePath = userStorageLocation.resolve(fileName).normalize();
            
            // Security check: ensure the file is within user's directory
            if (!filePath.startsWith(userStorageLocation)) {
                throw new FileStorageException("Access denied to file: " + fileName);
            }
            
            return Files.deleteIfExists(filePath);
        } catch (IOException ex) {
            throw new FileStorageException("Could not delete file: " + fileName, ex);
        }
    }

}

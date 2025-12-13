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
import java.util.List;
import java.util.stream.Stream;

@Service
public class FileStorageService {

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    private Path fileStorageLocation;

    @PostConstruct
    public void init() {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException ex) {
            throw new FileStorageException("Could not create the directory for file uploads.", ex);
        }
    }

    public List<FileUploadResponse> storeFiles(MultipartFile[] files) {
        List<FileUploadResponse> responses = new ArrayList<>();

        for (MultipartFile file : files) {
            responses.add(storeFile(file));
        }

        return responses;
    }

    public FileUploadResponse storeFile(MultipartFile file) {
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if (originalFileName.contains("..")) {
                throw new FileStorageException("Filename contains invalid path sequence: " + originalFileName);
            }

            String fileName = generateUniqueFileName(originalFileName);
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            String downloadUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/files/download/")
                    .path(fileName)
                    .toUriString();

            return FileUploadResponse.builder()
                    .fileName(fileName)
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

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
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

    public List<String> listAllFiles() {
        try (Stream<Path> paths = Files.walk(this.fileStorageLocation, 1)) {
            return paths
                    .filter(Files::isRegularFile)
                    .map(path -> path.getFileName().toString())
                    .toList();
        } catch (IOException ex) {
            throw new FileStorageException("Could not list files.", ex);
        }
    }

    public boolean deleteFile(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            return Files.deleteIfExists(filePath);
        } catch (IOException ex) {
            throw new FileStorageException("Could not delete file: " + fileName, ex);
        }
    }

    private String generateUniqueFileName(String originalFileName) {
        String baseName = originalFileName;
        String extension = "";

        int dotIndex = originalFileName.lastIndexOf('.');
        if (dotIndex > 0) {
            baseName = originalFileName.substring(0, dotIndex);
            extension = originalFileName.substring(dotIndex);
        }

        Path targetPath = this.fileStorageLocation.resolve(originalFileName);
        if (!Files.exists(targetPath)) {
            return originalFileName;
        }

        int counter = 1;
        String newFileName;
        do {
            newFileName = baseName + "_" + counter + extension;
            targetPath = this.fileStorageLocation.resolve(newFileName);
            counter++;
        } while (Files.exists(targetPath));

        return newFileName;
    }
}


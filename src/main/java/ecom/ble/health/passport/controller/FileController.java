package ecom.ble.health.passport.controller;

import ecom.ble.health.passport.model.FileUploadResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/api/files")
public interface FileController {

    @PostMapping("/upload")
    ResponseEntity<List<FileUploadResponse>> uploadFiles(@RequestParam("files") MultipartFile[] files);

    @GetMapping("/download/{filename}")
    ResponseEntity<Resource> downloadFile(@PathVariable String filename);

    @GetMapping("/list")
    ResponseEntity<List<String>> listAllFiles();

    @DeleteMapping("/{filename}")
    ResponseEntity<String> deleteFile(@PathVariable String filename);
}


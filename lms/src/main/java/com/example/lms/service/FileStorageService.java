package com.example.lms.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    // Define where to save files on your computer
    private final String UPLOAD_DIR = "uploads/";

    public String storeFile(MultipartFile file) {
        try {
            // 1. Create directory if it doesn't exist
            File directory = new File(UPLOAD_DIR);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // 2. Generate unique name to prevent overwrite
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR + fileName);

            // 3. Save bytes to disk
            Files.write(filePath, file.getBytes());

            // 4. Return the path (In production, this would be a full URL like http://localhost:8080/files/...)
            return "/uploads/" + fileName;

        } catch (IOException e) {
            throw new RuntimeException("Could not store file. Error: " + e.getMessage());
        }
    }
}
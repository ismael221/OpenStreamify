package com.ismael.movies.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class VideoUploadService {

    private final String tempDir = System.getProperty("java.io.tmpdir");

    public Path saveVideo(MultipartFile file) throws IOException {
        Path filePath = Path.of(tempDir, file.getOriginalFilename());
        Files.copy(file.getInputStream(), filePath);
        return filePath;
    }
}

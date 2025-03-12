package com.ismael.openstreamify.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class VideoUploadService {

    private static Logger logger = LoggerFactory.getLogger(VideoUploadService.class);

    private final String tempDir;

    public VideoUploadService() throws IOException {
        this.tempDir = System.getProperty("java.io.tmpdir") + File.separator + "raw";
        logger.info("Creating temporary directory at " + this.tempDir);
        Files.createDirectories(Paths.get(tempDir));
    }

    public Path saveVideo(MultipartFile file, String fileName) throws IOException {
        String originalFileName = file.getOriginalFilename();
        if (originalFileName != null && !fileName.contains(".")) {
            String extension = originalFileName.substring(originalFileName.lastIndexOf('.'));
            fileName += extension;
        }
        Path filePath = Paths.get(tempDir, fileName);
        Files.copy(file.getInputStream(), filePath);
        logger.info("Saving file " + fileName);
        return filePath;
    }
}

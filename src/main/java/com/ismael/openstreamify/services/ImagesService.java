package com.ismael.openstreamify.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class ImagesService {

    private  static  final Logger logger = LoggerFactory.getLogger(ImagesService.class);

    //Upload directory configuration
    private final Path uploadDir = Paths.get("uploads");

    //Base URL to access files
    @Value("${server.url}")
    private String serverUrl;

    public ImagesService() throws IOException {
        // Create upload directory if it doesn't exist
        Files.createDirectories(uploadDir);
    }

    public String uploadFile(MultipartFile file) throws IOException {
        // Generate a unique file name
        String filename = System.currentTimeMillis() + "-" + file.getOriginalFilename();
        Path filePath = uploadDir.resolve(filename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Returns the image URL return serverUrl + "/api/v1/media/img/" + filename;
        return serverUrl + "/api/v1/media/img/" + filename;
    }

    public Resource getFile(String filename) throws IOException {
        Path filePath = uploadDir.resolve(filename).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if (resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            logger.error("Arquivo não encontrado {}", filename);
            throw new RuntimeException("Arquivo não encontrado: " + filename);
        }
    }
}

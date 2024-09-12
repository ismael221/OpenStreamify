package com.ismael.movies.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class HlsService {

    private final Path baseLocation = Paths.get("videos/hls");

    private static final Logger logger = LoggerFactory.getLogger(HlsService.class);

    public HlsService() {
        // Cria o diretório "videos/hls" se ele não existir
        try {
            Files.createDirectories(baseLocation);
        } catch (IOException e) {
            logger.error("Could not create directory: " + baseLocation.toString(), e);
            throw new RuntimeException("Could not create upload directory!");
        }
    }

    public Resource getHlsResource(String filename) throws MalformedURLException {
        Path filePath = baseLocation.resolve(filename).normalize();
        Resource resource = new UrlResource(filePath.toUri());
        if (resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            logger.error("Resource not found: " + filename);
            throw new RuntimeException("Resource not found: " + filename);
        }
    }

}

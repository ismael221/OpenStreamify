package com.ismael.movies.services;

import org.springframework.stereotype.Service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class HlsService {

    private final Path baseLocation  = Paths.get("videos/hls");

    public Resource getHlsResource(String movieFolder, String filename) throws MalformedURLException {
        Path movieFolderPath = baseLocation.resolve(movieFolder).normalize();
        Path filePath = movieFolderPath.resolve(filename).normalize();
        Resource resource = new UrlResource(filePath.toUri());
        if (resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            throw new RuntimeException("Resource not found: " + filename);
        }
    }


}

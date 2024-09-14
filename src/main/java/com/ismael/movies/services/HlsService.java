package com.ismael.movies.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

@Service
public class HlsService {

    private final Path baseLocation = Paths.get("videos/hls");
    private static final Logger logger = LoggerFactory.getLogger(HlsService.class);

    @Autowired
    private StringRedisTemplate redisTemplate;

    public HlsService() {
        // Cria o diretório "videos/hls" se ele não existir
        try {
            Files.createDirectories(baseLocation);
        } catch (IOException e) {
            logger.error("Could not create directory: {}", baseLocation.toString(), e);
            throw new RuntimeException("Could not create upload directory!");
        }
    }

    public Resource getHlsResource(String filename) throws MalformedURLException {
        String cacheKey = "hls:" + filename;

        // Tenta buscar o arquivo do cache Redis
        String cachedFile = redisTemplate.opsForValue().get(cacheKey);
        if (cachedFile != null) {
            logger.info("Cache hit: {}", filename);
            Path cachedFilePath = Paths.get(cachedFile);
            return new UrlResource(cachedFilePath.toUri());
        }

        // Se o arquivo não estiver no cache, carrega do sistema de arquivos
        Path filePath = baseLocation.resolve(filename).normalize();
        logger.info("Fetching resource from disk: {}", filePath.toString());

        Resource resource = new UrlResource(filePath.toUri());
        if (resource.exists() && resource.isReadable()) {
            // Armazena o caminho do arquivo no cache Redis por 10 minutos
            redisTemplate.opsForValue().set(cacheKey, filePath.toString(), 10, TimeUnit.MINUTES);
            logger.info("Resource cached: {}", filename);
            return resource;
        } else {
            logger.error("Resource not found or not readable: {}", filename);
            throw new ResourceNotFoundException("Resource not found: " + filename);
        }
    }

    // Exceção personalizada para o caso de recursos não encontrados
    public static class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) {
            super(message);
        }
    }

}

package com.ismael.movies.services;

import com.ismael.movies.config.MinioConfig;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.InputStream;


@Service
public class HlsService {

    private final MinioClient minioClient;
    private final MinioConfig minioConfig;
    private final Logger logger = LoggerFactory.getLogger(HlsService.class);

    public HlsService(MinioClient minioClient, MinioConfig minioConfig) {
        this.minioClient = minioClient;
        this.minioConfig = minioConfig;
    }

    public Resource getHlsResource(String filename) throws Exception {
        String objectName = "hls/" + filename; // Caminho do arquivo no MinIO

        // Busca diretamente no MinIO
        InputStream stream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(minioConfig.getStreamBucket())
                        .object(objectName)
                        .build());

        logger.info("Fetching resource from MinIO: {}", objectName);

        return new InputStreamResource(stream);
    }

    // Exceção personalizada para o caso de recursos não encontrados
    public static class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) {
            super(message);
        }
    }
}

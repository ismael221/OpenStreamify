package com.ismael.openstreamify.services;

import com.ismael.openstreamify.config.MinioConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;


@Service
@RequiredArgsConstructor
public class MinioService {

    //TODO Find a way to cache it on Redis first and then retrieve from it, if exists on cache;

    private final MinioClient minioClient;
    private final MinioConfig minioConfig;
    private final Logger logger = LoggerFactory.getLogger(MinioService.class);

    public Resource getHlsResource(String filename) throws Exception {
        String objectName = "hls/" + filename; // Caminho do arquivo no MinIO

        //Search directly in MinIO
        InputStream stream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(minioConfig.getStreamBucket())
                        .object(objectName)
                        .build());

        logger.info("Fetching resource from MinIO: {}", objectName);

        return new InputStreamResource(stream);
    }

}

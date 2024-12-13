package com.ismael.movies.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;
import io.minio.MinioClient;
import okhttp3.OkHttpClient;
import java.util.concurrent.TimeUnit;


@Configuration
@Data
public class MinioConfig {

    @Value("${minio.access-key}")
    private String accessKey;

    @Value("${minio.secret-key}")
    private String secretKey;

    @Value("${minio.endpoint}")
    private String url;

    //@Value("${minio.bucket.store}")
    //private String storeBucket;

    @Value("${minio.bucket.stream}")
    private String streamBucket;

    private static final Logger logger = LoggerFactory.getLogger(MinioConfig.class);

    @Bean
    public MinioClient minioClient() throws Exception {

        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0]; // Trust in all certificates
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        //Does not verify anything
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        //Does not verify anything
                    }
                }
        };

        // Install TrustManager that bypasses certificate verification
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

        // Configure OkHttpClient to bypass SSL
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCerts[0])
                .hostnameVerifier((hostname, session) -> true) // Bypass hostname check
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();


        MinioClient minioClient =
                MinioClient.builder().endpoint(url).credentials(accessKey, secretKey).httpClient(httpClient).build();
        initBucket(minioClient, streamBucket);
        //initBucket(minioClient, storeBucket);
        return minioClient;
    }

    private void initBucket(MinioClient minioClient, String bucket) throws Exception {
        boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
        if (!exists) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            logger.info("successfully create bucket {} !", bucket);
        }
    }
}

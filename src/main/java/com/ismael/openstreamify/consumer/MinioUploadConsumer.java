package com.ismael.openstreamify.consumer;

import com.ismael.openstreamify.config.MinioConfig;
import com.ismael.openstreamify.config.RabbitMQConfig;
import com.ismael.openstreamify.model.Video;
import com.ismael.openstreamify.services.VideosService;
import com.ismael.openstreamify.services.NotificationService;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
public class MinioUploadConsumer {

    final
    MinioClient minioClient;
    final
    MinioConfig minioConfig;
    final
    VideosService videosService;
    final
    NotificationService notificationService;

    private static final Logger logger = LoggerFactory.getLogger(MinioUploadConsumer.class);

    public MinioUploadConsumer(MinioClient minioClient, MinioConfig minioConfig, VideosService videosService, NotificationService notificationService) {
        this.minioClient = minioClient;
        this.minioConfig = minioConfig;
        this.videosService = videosService;
        this.notificationService = notificationService;
    }

    @Async
    @RabbitListener(queues = RabbitMQConfig.MINIO_QUEUE)
    public void uploadVideo(String ridFilme) throws Exception {
        String tempDir = System.getProperty("java.io.tmpdir") + "/hls/" + ridFilme + "/";
        if (isMinioOnline()) {
            uploadFilesToMinIO(tempDir,ridFilme);
        } else {
            // If MinIO is not online, a message remains in the queue for reprocessing
            throw new RuntimeException("MinIO offline - upload attempt failed");
        }
    }

    private boolean isMinioOnline() {
        // Check MinIO status
        return true;
    }

    private void uploadFilesToMinIO(String tempDir, String ridFilme) throws Exception {
        File dir = new File(tempDir);
        System.out.println(dir);
        File[] files = dir.listFiles((dir1, name) -> name.endsWith(".ts") || name.endsWith(".m3u8") || name.endsWith(".vtt") || name.endsWith(".srt"));
        System.out.printf("Trying to upload ..." );
        if (files != null) {
            for (File file : files) {
                try (InputStream stream = new FileInputStream(file)) {
                    // String objectName = "hls/" + ridFilme + "/" + file.getName();
                    String objectName = "hls/" + file.getName();
                    System.out.println(objectName);
                    minioClient.putObject(
                            PutObjectArgs.builder()
                                    .bucket(minioConfig.getStreamBucket())
                                    .object(objectName)
                                    .stream(stream, file.length(), -1)
                                    .contentType("application/vnd.apple.mpegurl")
                                    .build());
                    logger.info("File {} sent to MinIO.", file.getName());
                }
            }
            Video newVideo = videosService.getMovieByRID(UUID.fromString(ridFilme));
            String mensagem = "🎬 *Processamento de Filme Concluído*\n\n" +
                    "O filme *" + newVideo.getTitle() + "* (ID: " + newVideo.getRid() + ") foi tratado com sucesso.\n" +
                    "Data de conclusão: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + "\n\n" +
                    "✔️ O arquivo está pronto para uso.";

            notificationService.enviarMensagemTelegram(mensagem);
            notificationService.notifyAllUsers("Novo filme disponivel: "+ newVideo.getTitle());
            String rawDir = System.getProperty("java.io.tmpdir") + "/raw/" + ridFilme + "/";
            cleanUpLocalFragmentFiles(tempDir);
            cleanUpLocalRawFiles(rawDir);
        }
    }

    private void cleanUpLocalFragmentFiles(String tempDir) throws IOException {
        File dir = new File(tempDir);
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    Files.deleteIfExists(file.toPath());
                }
            }
        }
        Files.deleteIfExists(Paths.get(tempDir));  // Remove temporary directory
    }

    private void cleanUpLocalRawFiles(String tempDir) throws IOException {
        File dir = new File(tempDir);
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    Files.deleteIfExists(file.toPath());
                }
            }
        }
        Files.deleteIfExists(Paths.get(tempDir));  // Remove temporary directory
    }
}

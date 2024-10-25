package com.ismael.movies.consumer;

import com.ismael.movies.DTO.VideoDTO;
import com.ismael.movies.config.MinioConfig;
import com.ismael.movies.config.RabbitMQConfig;
import com.ismael.movies.model.Movie;
import com.ismael.movies.services.FFmpegHLS;
import com.ismael.movies.services.MoviesService;
import com.ismael.movies.services.NotificationService;
import com.ismael.movies.services.UserService;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.hibernate.validator.internal.constraintvalidators.bv.time.futureorpresent.FutureOrPresentValidatorForYear;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Component
public class VideoUploadConsumer {

    @Autowired
    MinioClient minioClient;
    @Autowired
    MinioConfig minioConfig;
    @Autowired
    MoviesService moviesService;
    @Autowired
    UserService userService;
    @Autowired
    NotificationService notificationService;

    private static final Logger logger = LoggerFactory.getLogger(VideoUploadConsumer.class);

    @RabbitListener(queues = RabbitMQConfig.MINIO_QUEUE)
    public void uploadVideo(UUID ridFilme) throws Exception {
        String tempDir = System.getProperty("java.io.tmpdir") + "/hls/" + ridFilme + "/";

        System.out.printf("Calling consumer");
        if (isMinioOnline()) {
            uploadFilesToMinIO(tempDir,ridFilme);
        } else {
            // Se o MinIO não estiver online, a mensagem permanecerá na fila para reprocessamento
            throw new RuntimeException("MinIO offline - tentativa de upload falhou");
        }
    }

    private boolean isMinioOnline() {
        // Verifica o status do MinIO
        return true; // Placeholder para lógica de verificação
    }

    private void uploadFilesToMinIO(String tempDir, UUID ridFilme) throws Exception {
        File dir = new File(tempDir);
        File[] files = dir.listFiles((dir1, name) -> name.endsWith(".ts") || name.endsWith(".m3u8"));
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
                    logger.info("Arquivo {} enviado para o MinIO.", file.getName());
                }
            }
            List<UUID> users = userService.findAllUsersId();
            Movie newMovie = moviesService.getMovieByRID(ridFilme);
            String mensagem = "🎬 *Processamento de Filme Concluído*\n\n" +
                    "O filme *" + newMovie.getTitle() + "* (ID: " + newMovie.getRid() + ") foi tratado com sucesso.\n" +
                    "Data de conclusão: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + "\n\n" +
                    "✔️ O arquivo está pronto para uso.";

            notificationService.enviarMensagemTelegram(mensagem);
            notificationService.sendNotification("Novo filme disponivel: "+ newMovie.getTitle(),users);
            cleanUpLocalFiles(tempDir);
        }
    }

    private void cleanUpLocalFiles(String tempDir) throws IOException {
        File dir = new File(tempDir);
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    Files.deleteIfExists(file.toPath());
                }
            }
        }
        Files.deleteIfExists(Paths.get(tempDir));  // Remove o diretório temporário
    }
}

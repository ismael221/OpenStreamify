package com.ismael.movies.controller;

import com.ismael.movies.config.MinioConfig;
import com.ismael.movies.model.Movie;
import com.ismael.movies.model.Users.User;
import com.ismael.movies.services.*;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.errors.MinioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;
import java.nio.file.Path;
import java.nio.file.Paths;


@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/v1/media")
public class MediaRestController {
    @Autowired
    private HlsService hlsService;

    @Autowired
    MinioClient minioClient;

    @Autowired
    MinioConfig minioConfig;

    @Autowired
    private FFmpegHLS fFmpegHLS;

    @Autowired
    NotificationService notificationService;

    @Autowired
    MoviesService moviesService;

    @Autowired
    UserService userService;

    private final Path uploadDir = Paths.get("uploads");

    @Autowired
    private ImagesService imagesService;

    @GetMapping("/hls/{filename:.+}")
    public ResponseEntity<Resource> getHlsFile(@PathVariable String filename) {
        try {
            // Defina o nome do bucket e o caminho do arquivo que você quer acessar no bucket
            // Nome do bucket

            //TODO Corrigir e colocar a logica de pegar o video no bucket dentro do service e adicionar a logica do REDIS para verificar em cache primeiro e depois no bucket se necessario
            String objectName = "hls/" + filename; // Caminho do arquivo no bucket

            // Usando MinIO para obter o objeto (arquivo)
            InputStream stream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(minioConfig.getStreamBucket())
                            .object(objectName)
                            .build()
            );

            // Retorna o arquivo como um recurso de fluxo de entrada
            Resource resource = new InputStreamResource(stream);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "application/vnd.apple.mpegurl")
                    .body(resource);

        } catch (MinioException e) {
            // Log de erro
            System.err.println("Erro ao acessar o arquivo no MinIO: " + e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            // Log de erro genérico
            System.err.println("Erro desconhecido: " + e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }


    @PostMapping("/hls/upload")
    public ResponseEntity<String> uploadVideo(@RequestParam("file") MultipartFile file, @RequestParam UUID rid) {

        String uploadDir = System.getProperty("user.dir") + File.separator + "temp";

        File directory = new File(uploadDir);

        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (!created) {
                return ResponseEntity.status(500).body("Failed to create the upload directory.");
            }
        }


        File destFile = new File(uploadDir + File.separator + file.getOriginalFilename());

        try {
            // Transferindo o arquivo enviado para o destino
            file.transferTo(destFile);

            // Chamando o método para processar o vídeo com FFmpeg
            fFmpegHLS.executeFFmpegCommand(destFile.getAbsolutePath(), rid);
            Movie newMovie = moviesService.getMovieByRID(rid);
            List<UUID> users = userService.findAllUsersId();
            notificationService.sendNotification("Novo filme disponivel: "+ newMovie.getTitle(),users);
            return ResponseEntity.ok("Video uploaded and processed successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to upload and process the video.");
        }
    }

    @Value("${server.url}")
    private String serverUrl;
    public MediaRestController() throws IOException {
        // Cria o diretório de upload se não existir
        Files.createDirectories(uploadDir);
    }

    @PostMapping("/img/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String fileUrl = imagesService.uploadFile(file);
            return ResponseEntity.ok(fileUrl);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Erro ao salvar a imagem.");
        }
    }

    @GetMapping("/img/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        try {
            Resource resource = imagesService.getFile(filename);
            // Determinar o tipo de conteúdo baseado na extensão do arquivo
            String contentType = Files.probeContentType(resource.getFile().toPath());
            if (contentType == null) {
                contentType = "application/octet-stream"; // Content-Type genérico
            }

            return ResponseEntity.ok()
                    .header("Content-Type", contentType)
                    .body(resource);
        } catch (MalformedURLException e) {
            return ResponseEntity.status(500).build();
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }

}

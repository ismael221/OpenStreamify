package com.ismael.movies.controller;

import com.ismael.movies.model.Exceptions.MediaNotProcessedException;
import com.ismael.movies.model.Exceptions.ResourceNotFoundException;
import com.ismael.movies.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;



@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/v1/media")
public class MediaRestController {
    @Autowired
    private MinioService minioService;

    private final Path uploadDir = Paths.get("uploads");

    @Autowired
    private ImagesService imagesService;

    @Autowired
    private  VideoUploadService videoUploadService;

    @Autowired
    private VideoProcessingQueueService videoProcessingQueue;

    @Autowired
    private  NotificationService notificationService;

    @GetMapping("/hls/{filename:.+}")
    public ResponseEntity<Resource> getHlsFile(@PathVariable String filename) {
        try {
            // Tenta obter o arquivo do serviço
            Resource resource = minioService.getHlsResource(filename);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "application/vnd.apple.mpegurl") // Define o content type adequado para HLS
                    .body(resource);
        } catch (ResourceNotFoundException e) {
            // Caso o arquivo não seja encontrado
            System.err.println("Erro: Arquivo não encontrado - " + e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {

            // Log de erro genérico
            System.err.println("Erro desconhecido: " + e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }


    @PostMapping("/hls/upload")
    public ResponseEntity<String> uploadVideo(@RequestParam("file") MultipartFile file, @RequestParam("rid") String StringRid) throws IOException {
        try {
            Path savedPath = videoUploadService.saveVideo(file,StringRid);
            videoProcessingQueue.queueForProcessing(savedPath.toString());
            return ResponseEntity.ok("Upload successful, video queued for processing.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload video.");
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
            throw new MediaNotProcessedException("Error while saving image");
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

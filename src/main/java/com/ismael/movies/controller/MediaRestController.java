package com.ismael.movies.controller;

import com.ismael.movies.services.FFmpegHLS;
import com.ismael.movies.services.HlsService;
import com.ismael.movies.services.ImagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.util.UUID;
import java.nio.file.Path;
import java.nio.file.Paths;


@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/v1/media")
public class MediaRestController {
    @Autowired
    private HlsService hlsService;

    private final Path uploadDir = Paths.get("uploads");

    @Autowired
    private ImagesService imagesService;

    @GetMapping("/hls/{filename:.+}")
    public ResponseEntity<Resource> getHlsFile(@PathVariable String filename) {
        try {
            Resource resource = hlsService.getHlsResource(filename);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "application/vnd.apple.mpegurl")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Autowired
    private FFmpegHLS fFmpegHLS;

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

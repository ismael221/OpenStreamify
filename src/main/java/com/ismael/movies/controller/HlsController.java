package com.ismael.movies.controller;

import com.ismael.movies.services.FFmpegHLS;
import com.ismael.movies.services.HlsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/hls")
public class HlsController {
    @Autowired
    private HlsService hlsService;

    @GetMapping("/{filename:.+}")
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

    @PostMapping("/upload")
    public ResponseEntity<String> uploadVideo(@RequestParam("file") MultipartFile file, @RequestParam UUID rid) {
        // Definindo o diretório de destino
        String uploadDir = "C:\\Users\\Usuario\\Downloads\\resource\\";

        // Criando o arquivo no sistema
        File destFile = new File(uploadDir + file.getOriginalFilename());

        try {
            // Transferindo o arquivo enviado para o destino
            file.transferTo(destFile);

            // Chamando o método para processar o vídeo com FFmpeg
            fFmpegHLS.executeFFmpegCommand(destFile.getAbsolutePath(),rid);

            return ResponseEntity.ok("Video uploaded and processed successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to upload and process the video.");
        }
    }


}

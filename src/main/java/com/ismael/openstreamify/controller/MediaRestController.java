package com.ismael.openstreamify.controller;

import com.ismael.openstreamify.model.Exceptions.MediaNotProcessedException;
import com.ismael.openstreamify.model.Exceptions.ResourceNotFoundException;
import com.ismael.openstreamify.services.*;
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

    private final MinioService minioService;

    private final Path uploadDir = Paths.get("uploads");

    private final ImagesService imagesService;

    private final VideoUploadService videoUploadService;

    private final VideoProcessingQueueService videoProcessingQueue;

    private final NotificationService notificationService;

    @GetMapping("/hls/{filename:.+}")
    public ResponseEntity<Resource> getHlsFile(@PathVariable String filename) {
        try {
            //Try to get the service file
            Resource resource = minioService.getHlsResource(filename);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "application/vnd.apple.mpegurl") // Define o content type adequado para HLS
                    .body(resource);
        } catch (ResourceNotFoundException e) {
            // If the file is not found
            System.err.println("Error: File not found - " + e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {

            // Generic log error
            System.err.println("Unknown error: " + e.getMessage());
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
    public MediaRestController(MinioService minioService, ImagesService imagesService, VideoUploadService videoUploadService, VideoProcessingQueueService videoProcessingQueue, NotificationService notificationService) throws IOException {
        // Create upload directory if it doesn't exist
        Files.createDirectories(uploadDir);
        this.minioService = minioService;
        this.imagesService = imagesService;
        this.videoUploadService = videoUploadService;
        this.videoProcessingQueue = videoProcessingQueue;
        this.notificationService = notificationService;
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
            // Determine content type based on file extension
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

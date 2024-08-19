package com.ismael.movies.controller;

import com.ismael.movies.services.HlsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hls")
public class HlsController {
    @Autowired
    private HlsService hlsService;

    @GetMapping("/{folder}/{filename:.+}")
    public ResponseEntity<Resource> getHlsFile(@PathVariable String folder ,@PathVariable String filename) {
        try {
            Resource resource = hlsService.getHlsResource(folder,filename);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "application/vnd.apple.mpegurl")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}

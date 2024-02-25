package com.ismael.movies.controller;

import com.ismael.movies.model.Filme;
import com.ismael.movies.model.Media;
import com.ismael.movies.services.FilmeService;
import com.ismael.movies.services.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.event.InputEvent;
import java.io.InputStream;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/media")
public class MediaController {
    @Autowired
    MediaService mediaService;
    @Autowired
    FilmeService filmeService;

    @PostMapping("/{id_filme}/upload")
    public ResponseEntity filmeUpload(@RequestBody byte[] file, @PathVariable UUID id_filme){
        Filme filme = filmeService.getFilmePorRid(id_filme);
        try {
            byte[] arquivo = file;
            Media media = new Media();
            media.setData(arquivo);
            media.setFilme(filme);
            Media mediaResponse = mediaService.mediaUpload(media);
            return  new ResponseEntity(mediaResponse, HttpStatus.CREATED);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id_media}/play")
    public ResponseEntity<byte[]> filmePlayer(@PathVariable UUID id_media){

        try {
         Media media =  mediaService.retrieveMedia(id_media);
            if (media != null && media.getData() != null) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.valueOf("video/mp4"));

                return new ResponseEntity<>(media.getData(), headers, HttpStatus.OK);
            } else {
                return ResponseEntity.notFound().build();
            }
        }catch (Exception e){
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

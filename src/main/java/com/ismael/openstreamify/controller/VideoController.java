package com.ismael.openstreamify.controller;

import com.ismael.openstreamify.DTO.VideoDTO;
import com.ismael.openstreamify.model.Video;
import com.ismael.openstreamify.services.VideosService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/movies")
@CrossOrigin("*")
public class VideoController {

    final
    VideosService videosService;

    public VideoController(VideosService videosService) {
        this.videosService = videosService;
    }

    @GetMapping
    public  ResponseEntity<List> getAllMovies(){
        List<VideoDTO> movies = videosService.listAllMovies();
        return new ResponseEntity<>(movies,HttpStatus.OK);
    }

    @GetMapping("{rid}")
    public ResponseEntity<Video> getVideoById(@PathVariable UUID rid){
        Video videoEncontrado = videosService.getMovieByRID(rid);
        return  new ResponseEntity<>(videoEncontrado,HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<VideoDTO> newVideo(@Valid @RequestBody VideoDTO movie){
        var novoFilme = videosService.newMovie(movie);
        return  new ResponseEntity<>(novoFilme, HttpStatus.CREATED);
    }

    @PutMapping("{rid}")
    public  ResponseEntity<VideoDTO> updateVideo(@PathVariable UUID rid, @Valid @RequestBody VideoDTO movie){
        var filmeAtualizado = videosService.updateMovie(rid, movie);
        return  new ResponseEntity<>(filmeAtualizado,HttpStatus.OK);
    }

    @DeleteMapping("{rid}")
    public ResponseEntity deleteVideo(@PathVariable UUID rid){
        videosService.deleteMovie(rid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}

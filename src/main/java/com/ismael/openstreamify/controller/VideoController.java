package com.ismael.openstreamify.controller;

import com.ismael.openstreamify.DTO.MovieDTO;
import com.ismael.openstreamify.enums.VideoType;
import com.ismael.openstreamify.model.Genre;
import com.ismael.openstreamify.model.Video;
import com.ismael.openstreamify.services.VideosService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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
    public ResponseEntity<List> getAllMovies() {
        List<MovieDTO> movies = videosService.listAllMovies();
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @GetMapping("{rid}")
    public ResponseEntity<MovieDTO> getMovieById(@PathVariable UUID rid) {
        Video videoEncontrado = videosService.getMovieByRID(rid);
        if (videoEncontrado.getVideoType() == VideoType.MOVIE) {
            MovieDTO movieDTO = new MovieDTO();
            movieDTO.setId(movieDTO.getId());
            movieDTO.setTitle(movieDTO.getTitle());
            movieDTO.setSynopsis(movieDTO.getSynopsis());
            movieDTO.setTrailerUrl(movieDTO.getTrailerUrl());
            movieDTO.setVideoType(movieDTO.getVideoType());
            movieDTO.setBackgroundImgUrl(movieDTO.getBackgroundImgUrl());
            movieDTO.setCoverImgUrl(movieDTO.getCoverImgUrl());
            return new ResponseEntity<>(movieDTO, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<MovieDTO> newVideo(@Valid @RequestBody MovieDTO movie) {
        var novoFilme = videosService.newMovie(movie);
        return new ResponseEntity<>(novoFilme, HttpStatus.CREATED);
    }

    @PutMapping("{rid}")
    public ResponseEntity<MovieDTO> updateVideo(@PathVariable UUID rid, @Valid @RequestBody MovieDTO movie) {
        var filmeAtualizado = videosService.updateMovie(rid, movie);
        return new ResponseEntity<>(filmeAtualizado, HttpStatus.OK);
    }

    @DeleteMapping("{rid}")
    public ResponseEntity deleteVideo(@PathVariable UUID rid) {
        videosService.deleteMovie(rid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}

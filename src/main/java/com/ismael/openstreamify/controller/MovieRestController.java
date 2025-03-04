package com.ismael.openstreamify.controller;

import com.ismael.openstreamify.DTO.MovieDTO;
import com.ismael.openstreamify.model.Movie;
import com.ismael.openstreamify.services.MoviesService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/movies")
@CrossOrigin("*")
public class MovieRestController {

    final
    MoviesService moviesService;

    public MovieRestController(MoviesService moviesService) {
        this.moviesService = moviesService;
    }

    @GetMapping
    public  ResponseEntity<List> getAllFilmes(){
        List<MovieDTO> movies = moviesService.listAllMovies();
        return new ResponseEntity<>(movies,HttpStatus.OK);
    }

    @GetMapping("{rid}")
    public ResponseEntity<Movie> getMovieById(@PathVariable UUID rid){
        Movie movieEncontrado = moviesService.getMovieByRID(rid);
        return  new ResponseEntity<>(movieEncontrado,HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<MovieDTO> newMovie(@Valid @RequestBody MovieDTO movie){
        var novoFilme = moviesService.newMovie(movie);
        return  new ResponseEntity<>(novoFilme, HttpStatus.CREATED);
    }

    @PutMapping("{rid}")
    public  ResponseEntity<MovieDTO> updateMovie(@PathVariable UUID rid,@Valid @RequestBody MovieDTO movie){
        var filmeAtualizado = moviesService.updateMovie(rid, movie);
        return  new ResponseEntity<>(filmeAtualizado,HttpStatus.OK);
    }

    @DeleteMapping("{rid}")
    public ResponseEntity deleteMovie(@PathVariable UUID rid){
        moviesService.deleteMovie(rid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}

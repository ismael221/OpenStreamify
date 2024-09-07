package com.ismael.movies.controller;

import com.ismael.movies.DTO.MovieDTO;
import com.ismael.movies.model.Movie;
import com.ismael.movies.services.MoviesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/filme")
@CrossOrigin("*")
public class MovieRestController {

    @Autowired
    MoviesService moviesService;

    @GetMapping("/listar")
    public  ResponseEntity<List> getAllFilmes(){
        List<MovieDTO> movies = moviesService.listAllMovies();
        return new ResponseEntity<>(movies,HttpStatus.OK);
    }

    @GetMapping("/{rid}")
    public ResponseEntity<Movie> getMovieById(@PathVariable UUID rid){
        Movie movieEncontrado = moviesService.getMovieByRID(rid);
        return  new ResponseEntity<>(movieEncontrado,HttpStatus.OK);
    }

    @PostMapping( value = "/adicionar")
    public ResponseEntity<MovieDTO> addMovie(@RequestBody MovieDTO movie){
        var novoFilme = moviesService.addMovie(movie);
        return  new ResponseEntity<>(novoFilme, HttpStatus.CREATED);
    }

    @PutMapping("/atualizar/{rid}")
    public  ResponseEntity<Movie> updateMovie(@PathVariable UUID rid, @RequestBody Movie movie){
        var filmeAtualizado = moviesService.updateMovie(rid, movie);
        return  new ResponseEntity<>(filmeAtualizado,HttpStatus.OK);
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity deleteMovie(@PathVariable UUID rid){
        moviesService.deleteMovie(rid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/rid/{rid}")
    public ResponseEntity<Movie> getMovieByRID(@PathVariable UUID rid){
        var filmeFound = moviesService.getMovieByRID(rid);
       if (filmeFound != null){
           return new ResponseEntity<>(filmeFound,HttpStatus.OK);
       }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
       }
    }
}

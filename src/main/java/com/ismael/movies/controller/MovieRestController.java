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

    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable Integer id){
        Movie movieEncontrado = moviesService.getMovieById(id);
        return  new ResponseEntity<>(movieEncontrado,HttpStatus.OK);
    }

    @PostMapping( value = "/adicionar")
    public ResponseEntity<MovieDTO> addMovie(@RequestBody MovieDTO movie){
        var novoFilme = moviesService.addMovie(movie);
        return  new ResponseEntity<>(novoFilme, HttpStatus.CREATED);
    }

    @PutMapping("/atualizar/{id}")
    public  ResponseEntity<Movie> updateMovie(@PathVariable Integer id, @RequestBody Movie movie){
        var filmeAtualizado = moviesService.updateMovie(id, movie);
        return  new ResponseEntity<>(filmeAtualizado,HttpStatus.OK);
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity deleteMovie(@PathVariable Integer id){
        moviesService.deleteMovie(id);
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

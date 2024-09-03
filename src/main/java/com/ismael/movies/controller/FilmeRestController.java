package com.ismael.movies.controller;

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
public class FilmeRestController {

    @Autowired
    MoviesService filmeService;

    @GetMapping("/listar")
    public  ResponseEntity<List> getAllFilmes(){
        List<Movie> movies = filmeService.listAllMovies();
        return new ResponseEntity<>(movies,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movie> getFilmePorId(@PathVariable Integer id){
        Movie movieEncontrado = filmeService.getMovieById(id);
        return  new ResponseEntity<>(movieEncontrado,HttpStatus.OK);
    }

    @PostMapping("/adicionar")
    public ResponseEntity<Movie> addFilme(@RequestBody Movie movie){
        var novoFilme = filmeService.addMovie(movie);
        return  new ResponseEntity<>(novoFilme, HttpStatus.CREATED);
    }

    @PutMapping("/atualizar/{id}")
    public  ResponseEntity<Movie> atualizarFilme(@PathVariable Integer id, @RequestBody Movie movie){
        var filmeAtualizado = filmeService.updateMovie(id, movie);
        return  new ResponseEntity<>(filmeAtualizado,HttpStatus.OK);
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity deletarFilme(@PathVariable Integer id){
        filmeService.deleteMovie(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/rid/{rid}")
    public ResponseEntity<Movie> listarFilmePorRid(@PathVariable UUID rid){
        var filmeFound = filmeService.getMovieByRID(rid);
       if (filmeFound != null){
           return new ResponseEntity<>(filmeFound,HttpStatus.OK);
       }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
       }
    }
}

package com.ismael.movies.controller;

import com.ismael.movies.model.Filme;
import com.ismael.movies.services.FilmeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/filme")
@CrossOrigin("*")
public class FilmeControllerRest {

    @Autowired
    FilmeService filmeService;

    @GetMapping("/listar")
    public  ResponseEntity<List> getAllFilmes(){
        List<Filme> filmes = filmeService.listaFilmes();
        return new ResponseEntity<>(filmes,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Filme> getFilmePorId(@PathVariable Integer id){
        Filme filmeEncontrado = filmeService.getFilmePorID(id);
        return  new ResponseEntity<>(filmeEncontrado,HttpStatus.OK);
    }

    @PostMapping("/adicionar")
    public ResponseEntity<Filme> addFilme(@RequestBody Filme filme){
        var novoFilme = filmeService.cadastrarFilme(filme);
        return  new ResponseEntity<>(novoFilme, HttpStatus.CREATED);
    }

    @PutMapping("/atualizar/{id}")
    public  ResponseEntity<Filme> atualizarFilme(@PathVariable Integer id,@RequestBody Filme filme){
        var filmeAtualizado = filmeService.atualizarFilme(id,filme);
        return  new ResponseEntity<>(filmeAtualizado,HttpStatus.OK);
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity deletarFilme(@PathVariable Integer id){
        filmeService.deletarFilme(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/rid/{rid}")
    public ResponseEntity<Filme> listarFilmePorRid(@PathVariable UUID rid){
        var filmeFound = filmeService.getFilmePorRid(rid);
       if (filmeFound != null){
           return new ResponseEntity<>(filmeFound,HttpStatus.OK);
       }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
       }
    }
}

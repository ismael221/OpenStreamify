package com.ismael.movies.controller;

import com.ismael.movies.model.Analise;
import com.ismael.movies.model.Filme;
import com.ismael.movies.service.AnaliseService;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/analise")
public class AnaliseControllerRest {

    @Autowired
    AnaliseService analiseService;

    @PostMapping("/adicionar")
    public ResponseEntity<Analise> addAnalise(@RequestBody Analise analise){
            var novaAnalise = analiseService.adicionarAnalise(analise);
            return  new ResponseEntity<>(novaAnalise, HttpStatus.CREATED);
    }

    @GetMapping("/buscar/{id}")
    public  ResponseEntity<List> getAnalisePorFilme(@PathVariable Integer id){
        List<Analise> analises = analiseService.listarAnalises(id);
        return new ResponseEntity<>(analises,HttpStatus.OK);
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Analise> atualizarAnalise(@PathVariable Integer id,@RequestBody Analise analise){
        var novaAnalise = analiseService.atualizarAnalise(id,analise);
        return new ResponseEntity<>(novaAnalise,HttpStatus.OK);
    }

    @DeleteMapping("/deletar/{id}")
    public  ResponseEntity<Boolean> deletarAnalise(@PathVariable Integer id){
        analiseService.deleteAnalise(id);
        return new ResponseEntity<>(true,HttpStatus.OK);
    }
}

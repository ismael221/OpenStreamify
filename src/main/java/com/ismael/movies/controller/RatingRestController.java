package com.ismael.movies.controller;

import com.ismael.movies.model.Rating;
import com.ismael.movies.services.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/analise")
public class RatingRestController {

    @Autowired
    RatingService ratingService;

    @PostMapping("/adicionar")
    public ResponseEntity<Rating> addAnalise(@RequestBody Rating analise){
            var novaAnalise = ratingService.addRating(analise);
            return  new ResponseEntity<>(novaAnalise, HttpStatus.CREATED);
    }

    @GetMapping("/buscar/{id}")
    public  ResponseEntity<List> getAnalisePorFilme(@PathVariable UUID rid){
        List<Rating> analises = ratingService.listRatingsByMovieRID(rid);
        return new ResponseEntity<>(analises,HttpStatus.OK);
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Rating> atualizarAnalise(@PathVariable Integer id, @RequestBody Rating analise){
        var novaAnalise = ratingService.updateRating(id,analise);
        return new ResponseEntity<>(novaAnalise,HttpStatus.OK);
    }

    @DeleteMapping("/deletar/{id}")
    public  ResponseEntity<Boolean> deletarAnalise(@PathVariable Integer id){
        ratingService.deleteRating(id);
        return new ResponseEntity<>(true,HttpStatus.OK);
    }
}

package com.ismael.movies.controller;

import com.ismael.movies.DTO.RatingDTO;
import com.ismael.movies.DTO.RatingResponseDTO;
import com.ismael.movies.model.Rating;
import com.ismael.movies.services.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/ratings")
public class RatingRestController {

    @Autowired
    RatingService ratingService;

    @PostMapping
    public ResponseEntity<RatingResponseDTO> newRating(@RequestBody RatingDTO ratingDTO){
            var novaAnalise = ratingService.addRating(ratingDTO);
            return  new ResponseEntity<>(novaAnalise, HttpStatus.CREATED);
    }

    @GetMapping
    public  ResponseEntity<List> ratingsList(){
        List<RatingResponseDTO> analises = ratingService.listRatings();
        return new ResponseEntity<>(analises,HttpStatus.OK);
    }

    @PutMapping("{rid}")
    public ResponseEntity<Rating> updateRatings(@PathVariable("rid") UUID rid, @RequestBody Rating analise){
        var novaAnalise = ratingService.updateRating(rid,analise);
        return new ResponseEntity<>(novaAnalise,HttpStatus.OK);
    }

    @DeleteMapping("{rid}")
    public  ResponseEntity<Boolean> deleteRating(@PathVariable("rid") UUID id){
        ratingService.deleteRating(id);
        return new ResponseEntity<>(true,HttpStatus.OK);
    }

    @GetMapping("{rid}")
    public ResponseEntity<Rating> retrieveRatingByRid(@PathVariable("rid") UUID rid){
        var ratingFound = ratingService.getRatingByRid(rid);
        return new ResponseEntity<>(ratingFound,HttpStatus.OK);
    }

}

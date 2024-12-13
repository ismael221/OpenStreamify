package com.ismael.movies.controller;

import com.ismael.movies.DTO.RatingDTO;
import com.ismael.movies.DTO.RatingResponseDTO;
import com.ismael.movies.model.Rating;
import com.ismael.movies.services.RatingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/ratings")
public class RatingRestController {

    final
    RatingService ratingService;

    public RatingRestController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PostMapping
    public ResponseEntity<RatingResponseDTO> newRating(@Valid @RequestBody RatingDTO ratingDTO){
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
    public ResponseEntity<RatingDTO> retrieveRatingByRid(@PathVariable("rid") UUID rid){
        var ratingFound = ratingService.getRatingDtoByRid(rid);
        return new ResponseEntity<>(ratingFound,HttpStatus.OK);
    }

    @GetMapping("movie/{rid}")
    public ResponseEntity<List> retrieveRatingByMovieRid(@PathVariable("rid") UUID rid){
        List<RatingDTO> list = ratingService.listRatingsByMovieRID(rid);
        return new ResponseEntity<>(list,HttpStatus.OK);
    }

}

package com.ismael.movies.services;

import com.ismael.movies.model.Rating;
import com.ismael.movies.repository.RatingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

//TODO Added the DTO class to fix the json infinite loop
@Service
public class RatingService {

    final
    RatingRepository ratingRepository;

    public RatingService(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    @Transactional
    public Rating addRating(Rating rating){
            ratingRepository.save(rating);
            return rating;
    }

    @Transactional
    public List<Rating> listRatings(){
        List<Rating> ratingsList = ratingRepository.findAll();
        return ratingsList;
    }

    @Transactional
    public List<Rating> listRatingsByMovieRID(UUID movieId){
         List<Rating> ratings =  ratingRepository.findByMovie_rid(movieId);
         return ratings;
    }

    @Transactional
    public Rating getRatingByRid(UUID rid){
          return  ratingRepository.findByRidEquals(rid).orElseThrow();
    }

    @Transactional
    public Rating updateRating(UUID rid, Rating analise){
            Rating a = getRatingByRid(rid);
            a.setMovie(analise.getMovie());
            a.setRating(analise.getRating());
            a.setComment(analise.getComment());
            ratingRepository.save(a);
            return a;
    }

    @Transactional
    public void deleteRating(UUID rid){
        ratingRepository.deleteByRid(rid);
    }
}

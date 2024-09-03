package com.ismael.movies.services;

import com.ismael.movies.model.Rating;
import com.ismael.movies.repository.RatingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingService {

    final
    RatingRepository ratingRepository;

    public RatingService(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    public Rating addRating(Rating rating){
            ratingRepository.save(rating);
            return rating;
    }

    public List<Rating> listRatingsByMovieId(Integer movieId){
         List<Rating> ratings =  ratingRepository.findByMovie_Id(movieId);
         return ratings;
    }

    public Rating getRatingById(Integer id){
          return  ratingRepository.findById(id).orElseThrow();
    }

    public Rating updateRating(Integer id, Rating analise){
            Rating a = getRatingById(id);
            a.setMovie(analise.getMovie());
            a.setRating(analise.getRating());
            a.setComment(analise.getComment());
            ratingRepository.save(a);
            return a;
    }

    public void deleteRating(Integer id){
        ratingRepository.deleteById(id);
    }
}

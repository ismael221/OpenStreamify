package com.ismael.movies.services;

import com.ismael.movies.DTO.MovieDTO;
import com.ismael.movies.DTO.RatingDTO;
import com.ismael.movies.DTO.RatingResponseDTO;
import com.ismael.movies.model.Movie;
import com.ismael.movies.model.Rating;
import com.ismael.movies.repository.RatingRepository;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

//TODO Added the DTO class to fix the json infinite loop
@Service
@CacheConfig(cacheNames = "ratings")
public class RatingService {

    final
    RatingRepository ratingRepository;

    final
    ModelMapper modelMapper;

    final
    MoviesService moviesService;

    public RatingDTO convertToDto(Rating rating){
        return modelMapper.map(rating, RatingDTO.class);
    }

    public RatingResponseDTO convertToResponseDTO(Rating rating){
        return modelMapper.map(rating, RatingResponseDTO.class);
    }

    public Rating convertToEntity(RatingDTO ratingDTO){
        return  modelMapper.map(ratingDTO, Rating.class);
    }


    public RatingService(RatingRepository ratingRepository, ModelMapper modelMapper, MoviesService moviesService) {
        this.ratingRepository = ratingRepository;
        this.modelMapper = modelMapper;
        this.moviesService = moviesService;
    }

    @Transactional
    @CacheEvict(cacheNames = "ratings-list", allEntries = true)
    public RatingResponseDTO addRating(RatingDTO rating){
            Rating newRating = convertToEntity(rating);
            Movie movie = moviesService.getMovieByRID(rating.getMovie());
            newRating.setMovie(movie);
            newRating.setCreatedAt(new Date());
            Rating saved =  ratingRepository.save(newRating);
            return convertToResponseDTO(saved);
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "ratings-list")
    public List<RatingResponseDTO> listRatings(){
        List<Rating> ratings = ratingRepository.findAll();
        List<RatingResponseDTO> ratingsList = ratings
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
        return ratingsList;
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "ratings-list")
    public List<RatingDTO> listRatingsByMovieRID(UUID movieId){
         List<Rating> ratings =  ratingRepository.findByMovie_rid(movieId);
         return ratings.stream()
                 .map(RatingDTO::from)
                 .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Cacheable
    public RatingDTO getRatingDtoByRid(UUID rid){
        Rating rating = ratingRepository.findByRidEquals(rid).orElseThrow();
        RatingDTO ratingDTO = RatingDTO.builder()
                .user(rating.getUser())
                .comment(rating.getComment())
                .createdAt(rating.getCreatedAt())
                .rating(rating.getRating())
                .rid(rating.getRid())
                .movie(rating.getMovie().getRid())
                .build();
          return ratingDTO;
    }

    public Rating getRatingByRid(UUID rid){
        return ratingRepository.findByRidEquals(rid).orElseThrow();
    }

    @Transactional
    @CachePut(cacheNames = "ratings-list",key = "#rid")
    public Rating updateRating(UUID rid, Rating analise){
            Rating a = getRatingByRid(rid);
            a.setMovie(analise.getMovie());
            a.setRating(analise.getRating());
            a.setComment(analise.getComment());
            ratingRepository.save(a);
            return a;
    }

    @Transactional
    @CacheEvict(cacheNames = "ratings-list",key = "#rid",allEntries = true)
    public void deleteRating(UUID rid){
        ratingRepository.deleteByRid(rid);
    }
}

package com.ismael.openstreamify.services;

import com.ismael.openstreamify.DTO.RatingDTO;
import com.ismael.openstreamify.DTO.RatingResponseDTO;
import com.ismael.openstreamify.model.Video;
import com.ismael.openstreamify.model.Rating;
import com.ismael.openstreamify.repository.RatingRepository;
import org.modelmapper.ModelMapper;
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
    VideosService videosService;

    public RatingDTO convertToDto(Rating rating){
        return modelMapper.map(rating, RatingDTO.class);
    }

    public RatingResponseDTO convertToResponseDTO(Rating rating){
        return modelMapper.map(rating, RatingResponseDTO.class);
    }

    public Rating convertToEntity(RatingDTO ratingDTO){
        return  modelMapper.map(ratingDTO, Rating.class);
    }


    public RatingService(RatingRepository ratingRepository, ModelMapper modelMapper, VideosService videosService) {
        this.ratingRepository = ratingRepository;
        this.modelMapper = modelMapper;
        this.videosService = videosService;
    }

    @Transactional
    @CacheEvict(cacheNames = "ratings-list", allEntries = true)
    public RatingResponseDTO addRating(RatingDTO rating){
            Rating newRating = convertToEntity(rating);
            Video video = videosService.getMovieByRID(rating.getMovie());
            newRating.setVideo(video);
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
         List<Rating> ratings =  ratingRepository.findByVideo_id(movieId);
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
                .movie(rating.getVideo().getId())
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
            a.setVideo(analise.getVideo());
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

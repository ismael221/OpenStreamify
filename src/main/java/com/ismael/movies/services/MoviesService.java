package com.ismael.movies.services;

import com.ismael.movies.DTO.MovieDTO;
import com.ismael.movies.model.Movie;
import com.ismael.movies.repository.MovieRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@CacheConfig(cacheNames = "movies")
public class MoviesService {
    @Autowired
    MovieRepository movieRepository;

    @Autowired
    ModelMapper modelMapper;

    private  static  final Logger logger = LoggerFactory.getLogger(MoviesService.class);

    public MovieDTO convertToDto(Movie movie){
        return modelMapper.map(movie, MovieDTO.class);
    }

    public Movie convertToEntity(MovieDTO movieDTO){
        return  modelMapper.map(movieDTO, Movie.class);
    }

    @Transactional
    @CachePut
    public MovieDTO newMovie(MovieDTO movie){
         Movie newMovie =  convertToEntity(movie);
         MovieDTO movieFound = convertToDto(movieRepository.save(newMovie));
        return movieFound;
    }
    //TODO Set a time for the cache to be alive, because when you delete the data on the database it still on redis cache
    @Transactional(readOnly = true)
    @Cacheable
    public List<MovieDTO> listAllMovies(){
        List<Movie>  moviesFoundList= movieRepository.findAll();
        List<MovieDTO> moviesListConverted =  moviesFoundList.stream().map(this::convertToDto).collect(Collectors.toList());
        return moviesListConverted;
    }

    @Transactional
    @CachePut
    public Movie updateMovie(UUID movieRid, Movie movieRequest){
            Movie movie = getMovieByRID(movieRid);
            movie.setGenres(movieRequest.getGenres());
            movie.setTitle(movieRequest.getTitle());
            movie.setSynopsis(movieRequest.getSynopsis());
            movie.setReleased(movieRequest.getReleased());
            movie.setBackgroundImgUrl(movieRequest.getBackgroundImgUrl());
            movie.setCoverImgUrl(movieRequest.getCoverImgUrl());
            movie.setTrailerUrl(movieRequest.getTrailerUrl());
            movieRepository.save(movie);
            return movie;
    }

    @Transactional
    @CacheEvict(allEntries = true)
    public void deleteMovie(UUID movieRid){
            Movie movie = getMovieByRID(movieRid);
            movieRepository.deleteById((int) movie.getId());
    }

    //Todo fix issue with the cache on the second retrieve, probably need to converto to DTO
    @Transactional(readOnly = true)
    @Cacheable
    public Movie getMovieByRID(UUID rid){
        Movie movie = movieRepository.getMoviesByRidIs(rid);
        return movie;
    }

}

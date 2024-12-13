package com.ismael.movies.services;

import com.ismael.movies.DTO.MovieDTO;
import com.ismael.movies.model.Exceptions.ResourceNotFoundException;
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
@Cacheable(cacheNames = "movies")
public class MoviesService {
    @Autowired
    MovieRepository movieRepository;
    //TODO FIX THE CACHEC EVICT AS ITS NOT UPDATING WHEN ADDING A NEW MOVIE 2

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
    @CacheEvict(cacheNames = "movies-list", allEntries = true)
    public MovieDTO newMovie(MovieDTO movie){
         Movie newMovie =  convertToEntity(movie);
         MovieDTO movieFound = convertToDto(movieRepository.save(newMovie));
        return movieFound;
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "movies-list")
    public List<MovieDTO> listAllMovies(){
        List<Movie>  moviesFoundList= movieRepository.findAll();
        List<MovieDTO> moviesListConverted =  moviesFoundList.stream().map(this::convertToDto).collect(Collectors.toList());
        return moviesListConverted;
    }

    @Transactional
    @CachePut(cacheNames = "movies-list", key = "#movieRid")
    public MovieDTO updateMovie(UUID movieRid, MovieDTO movieRequest){
            Movie movie = getMovieByRID(movieRid);
            movie.setGenres(movieRequest.getGenres());
            movie.setTitle(movieRequest.getTitle());
            movie.setSynopsis(movieRequest.getSynopsis());
            movie.setReleased(movieRequest.getReleased());
            movie.setBackgroundImgUrl(movieRequest.getBackgroundImgUrl());
            movie.setCoverImgUrl(movieRequest.getCoverImgUrl());
            movie.setTrailerUrl(movieRequest.getTrailerUrl());
            movieRepository.save(movie);
            return convertToDto(movie);
    }

    @Transactional
    @CacheEvict(cacheNames = "movies-list",allEntries = true,key = "#movieRid")
    public void deleteMovie(UUID movieRid){
            Movie movie = getMovieByRID(movieRid);
            movieRepository.delete(movie);
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "movie-by-rid",key = "#rid")
    public Movie getMovieByRID(UUID rid){
        Movie movie = movieRepository.getMoviesByRidIs(rid).orElseThrow(() -> new ResourceNotFoundException("Movie with " + rid +" not found"));
        return movie;
    }

}

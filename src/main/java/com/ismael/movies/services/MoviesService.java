package com.ismael.movies.services;

import com.ismael.movies.DTO.MovieDTO;
import com.ismael.movies.model.Movie;
import com.ismael.movies.repository.MovieRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
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
    public MovieDTO addMovie(MovieDTO movie){
         Movie newMovie =  convertToEntity(movie);
         MovieDTO movieFound = convertToDto(movieRepository.save(newMovie));
        return movieFound;
    }

    @Transactional(readOnly = true)
    public List<MovieDTO> listAllMovies(){
        List<Movie>  moviesFoundList= movieRepository.findAll();
        List<MovieDTO> moviesListConverted =  moviesFoundList.stream().map(this::convertToDto).collect(Collectors.toList());
        return moviesListConverted;
    }

    @Transactional
    public Movie updateMovie(UUID filmeID, Movie movieRequest){
            Movie movie = getMovieByRID(filmeID);
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
    public void deleteMovie(UUID filmeID){
            Movie movie = getMovieByRID(filmeID);
            movieRepository.deleteById((int) movie.getId());
    }

    @Transactional(readOnly = true)
    public Movie getMovieByRID(UUID rid){
        Movie movie = movieRepository.getMoviesByRidIs(rid);
        return movie;
    }

}
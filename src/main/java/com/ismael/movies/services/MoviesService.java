package com.ismael.movies.services;

import com.ismael.movies.model.Movie;
import com.ismael.movies.repository.MovieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class MoviesService {
    @Autowired
    MovieRepository movieRepository;

    private  static  final Logger logger = LoggerFactory.getLogger(MoviesService.class);

    public Movie addMovie(Movie movie){
        movieRepository.save(movie);

        return movie;
    }

    public List<Movie> listAllMovies(){
        //logger.info("Informação básica");
       // logger.debug("Informação detalhada para debug");
        //logger.error("Algo deu errado!");
        return movieRepository.findAll();
    }

    public Movie getMovieById(Integer movieId){
        return movieRepository.findById(movieId).orElseThrow();
    }

    public Movie updateMovie(Integer filmeID, Movie movieRequest){
            Movie movie = getMovieById(filmeID);
            movie.setGenre(movieRequest.getGenre());
            movie.setTitle(movieRequest.getTitle());
            movie.setSynopsis(movieRequest.getSynopsis());
            movie.setReleased(movieRequest.getReleased());
            movieRepository.save(movie);
            return movie;
    }

    public void deleteMovie(Integer filmeID){
            Movie movie = getMovieById(filmeID);
            movieRepository.deleteById((int) movie.getId());
    }

    public Movie getMovieByRID(UUID rid){
        Movie movie = movieRepository.getMoviesByRidIs(rid);
        return movie;
    }

}

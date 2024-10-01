package com.ismael.movies.repository;

import com.ismael.movies.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MovieRepository extends JpaRepository<Movie,Integer> {
   Optional<Movie> getMoviesByRidIs(UUID rid);
}

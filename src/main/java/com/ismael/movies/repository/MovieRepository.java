package com.ismael.movies.repository;

import com.ismael.movies.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MovieRepository extends JpaRepository<Movie,Integer> {
   Movie getMoviesByRidIs(UUID rid);
}

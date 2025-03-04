package com.ismael.openstreamify.repository;

import com.ismael.openstreamify.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MovieRepository extends JpaRepository<Movie,Integer> {
   Optional<Movie> getMoviesByRidIs(UUID rid);
}

package com.ismael.movies.repository;

import com.ismael.movies.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RatingRepository extends JpaRepository<Rating,Integer> {
    List<Rating> findByMovie_rid(UUID Rid);

}

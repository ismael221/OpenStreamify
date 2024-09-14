package com.ismael.movies.repository;

import com.ismael.movies.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RatingRepository extends JpaRepository<Rating,Integer> {
    List<Rating> findByMovie_rid(UUID Rid);
    Optional<Rating> findByRidEquals(UUID rid);

    @Modifying
    @Query("DELETE FROM Rating r WHERE r.rid = :rid")
    void deleteByRid(UUID rid);
}

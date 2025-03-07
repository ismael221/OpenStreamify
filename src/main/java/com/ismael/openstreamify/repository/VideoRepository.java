package com.ismael.openstreamify.repository;

import com.ismael.openstreamify.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VideoRepository extends JpaRepository<Video, Integer> {
    Optional<Video> getMoviesByRidIs(UUID rid);
}

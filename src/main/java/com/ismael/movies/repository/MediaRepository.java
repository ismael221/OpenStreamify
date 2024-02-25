package com.ismael.movies.repository;

import com.ismael.movies.model.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface MediaRepository extends JpaRepository<Media, UUID> {
    @Query("SELECT m.data FROM Media m WHERE m.rid = :uuid")
    Media findByRid(@Param("uuid") UUID uuid);
}

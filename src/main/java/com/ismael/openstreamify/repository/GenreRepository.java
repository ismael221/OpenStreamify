package com.ismael.openstreamify.repository;

import com.ismael.openstreamify.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GenreRepository extends JpaRepository<Genre, UUID> {

    @Query("SELECT n FROM Genre n WHERE n.rid= :rid")
    Optional<Genre> findByRid(@Param("rid") UUID rid);
}

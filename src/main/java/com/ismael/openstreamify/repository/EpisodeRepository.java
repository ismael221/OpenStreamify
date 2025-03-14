package com.ismael.openstreamify.repository;

import com.ismael.openstreamify.model.Episode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EpisodeRepository extends JpaRepository<Episode, UUID> {

    List<Episode> findBySeries_id(UUID Rid);
}

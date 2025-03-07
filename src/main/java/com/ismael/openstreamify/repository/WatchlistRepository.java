package com.ismael.openstreamify.repository;

import com.ismael.openstreamify.model.Users.User;
import com.ismael.openstreamify.model.Video;
import com.ismael.openstreamify.model.Watchlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WatchlistRepository extends JpaRepository<Watchlist, UUID> {

    List<Watchlist> findByUser(User user);

    Optional<Watchlist> findByUserAndVideo(User user, Video video);
}

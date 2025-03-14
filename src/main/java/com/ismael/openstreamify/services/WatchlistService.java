package com.ismael.openstreamify.services;

import com.ismael.openstreamify.model.Users.User;
import com.ismael.openstreamify.model.Video;
import com.ismael.openstreamify.model.Watchlist;
import com.ismael.openstreamify.repository.WatchlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WatchlistService {

    private final WatchlistRepository watchlistRepository;

    public List<Watchlist> getUserWatchlist(User user) {
        return watchlistRepository.findByUser(user);
    }

    public String addToWatchlist(User user, Video video) {
        Optional<Watchlist> existingEntry = watchlistRepository.findByUserAndVideo(user, video);
        if (existingEntry.isPresent()) {
            return "The video is already on your watchlist!";
        }

        Watchlist watchlist = new Watchlist();
        watchlist.setUser(user);
        watchlist.setVideo(video);
        watchlistRepository.save(watchlist);
        return "Video added to your watchlist!";
    }

    public String removeFromWatchlist(User user, Video video) {
        Optional<Watchlist> entry = watchlistRepository.findByUserAndVideo(user, video);
        if (entry.isPresent()) {
            watchlistRepository.delete(entry.get());
            return "Video removed from your watchlist!";
        }
        return "The video is not on your watchlist!";
    }
}

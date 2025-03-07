package com.ismael.openstreamify.services;

import com.ismael.openstreamify.model.Users.User;
import com.ismael.openstreamify.model.Video;
import com.ismael.openstreamify.model.Watchlist;
import com.ismael.openstreamify.repository.WatchlistRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WatchlistService {

    private final WatchlistRepository watchlistRepository;

    public WatchlistService(WatchlistRepository watchlistRepository) {
        this.watchlistRepository = watchlistRepository;
    }

    public List<Watchlist> getUserWatchlist(User user) {
        return watchlistRepository.findByUser(user);
    }

    public String addToWatchlist(User user, Video video) {
        Optional<Watchlist> existingEntry = watchlistRepository.findByUserAndVideo(user, video);
        if (existingEntry.isPresent()) {
            return "O vídeo já está na sua watchlist!";
        }

        Watchlist watchlist = new Watchlist();
        watchlist.setUser(user);
        watchlist.setVideo(video);
        watchlistRepository.save(watchlist);
        return "Vídeo adicionado à sua watchlist!";
    }

    public String removeFromWatchlist(User user, Video video) {
        Optional<Watchlist> entry = watchlistRepository.findByUserAndVideo(user, video);
        if (entry.isPresent()) {
            watchlistRepository.delete(entry.get());
            return "Vídeo removido da sua watchlist!";
        }
        return "O vídeo não está na sua watchlist!";
    }
}

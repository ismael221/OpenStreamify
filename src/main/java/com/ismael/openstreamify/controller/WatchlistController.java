package com.ismael.openstreamify.controller;

import com.ismael.openstreamify.model.Users.User;
import com.ismael.openstreamify.model.Video;
import com.ismael.openstreamify.model.Watchlist;
import com.ismael.openstreamify.services.VideosService;
import com.ismael.openstreamify.services.WatchlistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/watchlist")
public class WatchlistController {
    private final WatchlistService watchlistService;
    private final VideosService videosService;

    public WatchlistController(WatchlistService watchlistService, VideosService videosService) {
        this.watchlistService = watchlistService;
        this.videosService = videosService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Watchlist>> getWatchlist(@PathVariable UUID userId) {
        User user = new User();
        user.setId(userId);
        return ResponseEntity.ok(watchlistService.getUserWatchlist(user));
    }

    @PostMapping("/add/{userId}/{videoId}")
    public ResponseEntity<String> addToWatchlist(@PathVariable UUID userId, @PathVariable UUID videoId) {
        User user = new User();
        user.setId(userId);
        Video video = videosService.getMovieByRID(videoId);
        return ResponseEntity.ok(watchlistService.addToWatchlist(user, video));
    }

    @DeleteMapping("/remove/{userId}/{videoId}")
    public ResponseEntity<String> removeFromWatchlist(@PathVariable UUID userId, @PathVariable UUID videoId) {
        User user = new User();
        user.setId(userId);
        Video video = videosService.getMovieByRID(videoId);
        return ResponseEntity.ok(watchlistService.removeFromWatchlist(user, video));
    }
}

package com.ismael.openstreamify.controller;

import com.ismael.openstreamify.DTO.EpisodeRequestDTO;
import com.ismael.openstreamify.DTO.EpisodeResponseDTO;

import com.ismael.openstreamify.DTO.SerieResponseDTO;
import com.ismael.openstreamify.services.EpisodeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/series")
public class SerieController {

    final EpisodeService episodeService;

    public SerieController(EpisodeService episodeService) {
        this.episodeService = episodeService;
    }

    @PostMapping("/{rid}/episodes")
    public ResponseEntity<EpisodeResponseDTO> addNewEpisode(@PathVariable UUID rid, @RequestBody EpisodeRequestDTO episode) {
        EpisodeResponseDTO episode1 = episodeService.addNewEpisode(episode, rid);
        return new ResponseEntity<>(episode1, HttpStatus.CREATED);
    }

    @GetMapping("/{rid}/episodes")
    public ResponseEntity<SerieResponseDTO> retrieveAllEpisodes(@PathVariable UUID rid) {
        SerieResponseDTO episodes = episodeService.getAllSeriesEpisodes(rid);
        return new ResponseEntity<>(episodes, HttpStatus.OK);
    }

    @GetMapping("/{rid}")
    public ResponseEntity<EpisodeResponseDTO> retrieveEpisode(@PathVariable UUID rid) {
        return new ResponseEntity<>(episodeService.getEpisodeById(rid), HttpStatus.OK);
    }
}

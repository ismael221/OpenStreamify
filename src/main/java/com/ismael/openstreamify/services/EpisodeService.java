package com.ismael.openstreamify.services;

import com.ismael.openstreamify.DTO.EpisodeRequestDTO;
import com.ismael.openstreamify.DTO.EpisodeResponseDTO;
import com.ismael.openstreamify.DTO.SerieResponseDTO;
import com.ismael.openstreamify.model.Episode;
import com.ismael.openstreamify.model.Video;
import com.ismael.openstreamify.repository.EpisodeRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class EpisodeService {

    final EpisodeRepository episodeRepository;
    final VideosService videosService;

    public EpisodeService(EpisodeRepository episodeRepository, VideosService videosService) {
        this.episodeRepository = episodeRepository;
        this.videosService = videosService;
    }

    public EpisodeResponseDTO addNewEpisode(EpisodeRequestDTO episode, UUID rid) {

        Video serie = videosService.getMovieByRID(rid);

        if (serie != null) {
            Episode newEpisode = new Episode();
            newEpisode.setEpisodeNumber(episode.getEpisodeNumber());
            newEpisode.setSeries(serie);
            newEpisode.setDetails(episode.getDetails());
            newEpisode.setSeason(episode.getSeason());
            newEpisode.setCoverImg(episode.getCoverImg());
            newEpisode.setTitle(episode.getTitle());

            Episode response = episodeRepository.save(newEpisode);
            EpisodeResponseDTO responseDTO = new EpisodeResponseDTO(
                    response.getId(),
                    response.getTitle(),
                    response.getSeason(),
                    response.getEpisodeNumber(),
                    response.getCoverImg(),
                    response.getDetails()
            );
            return responseDTO;
        }
        return null;
    }

    public SerieResponseDTO getAllSeriesEpisodes(UUID serieID) {

        Video serie = videosService.getMovieByRID(serieID);
        SerieResponseDTO responseDTO = new SerieResponseDTO();
        List<EpisodeResponseDTO> episodesDto = new ArrayList<>();
        List<Episode> episodes = episodeRepository.findBySeries_id(serieID);

        if (serie != null) {
            responseDTO.setReleased(serie.getReleased());
            responseDTO.setSynopsis(serie.getSynopsis());
            responseDTO.setTrailerUrl(serie.getTrailerUrl());
            responseDTO.setVideoType(serie.getVideoType());
            responseDTO.setTitle(serie.getTitle());
            responseDTO.setBackgroundImgUrl(serie.getBackgroundImgUrl());
            responseDTO.setCoverImgUrl(serie.getCoverImgUrl());
            responseDTO.setReleased(serie.getReleased());
            responseDTO.setId(serie.getId());

            for (Episode episode : episodes) {

                EpisodeResponseDTO episodeResponseDTO = getEpisodeById(episode.getId());

                episodesDto.add(episodeResponseDTO);
            }
            responseDTO.setEpisodes(episodesDto);

            return responseDTO;
        }

        return null;
    }

    public EpisodeResponseDTO getEpisodeById(UUID id) {
        Episode episode = episodeRepository.findById(id).orElseThrow();
        EpisodeResponseDTO responseDTO = new EpisodeResponseDTO(
                episode.getId(),
                episode.getTitle(),
                episode.getSeason(),
                episode.getEpisodeNumber(),
                episode.getCoverImg(),
                episode.getDetails());

        return responseDTO;
    }
}

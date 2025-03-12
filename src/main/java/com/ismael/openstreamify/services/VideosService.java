package com.ismael.openstreamify.services;

import com.ismael.openstreamify.DTO.VideoDTO;
import com.ismael.openstreamify.model.Exceptions.ResourceNotFoundException;
import com.ismael.openstreamify.model.Video;
import com.ismael.openstreamify.repository.VideoRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Cacheable(cacheNames = "movies")
public class VideosService {
    //TODO FIX THE CACHEC EVICT AS ITS NOT UPDATING WHEN ADDING A NEW MOVIE 2
    private final VideoRepository videoRepository;
    private final ModelMapper modelMapper;

    private static final Logger logger = LoggerFactory.getLogger(VideosService.class);

    public VideosService(VideoRepository videoRepository, ModelMapper modelMapper) {
        this.videoRepository = videoRepository;
        this.modelMapper = modelMapper;
    }

    public VideoDTO convertToDto(Video video) {
        return modelMapper.map(video, VideoDTO.class);
    }

    public Video convertToEntity(VideoDTO videoDTO) {
        return modelMapper.map(videoDTO, Video.class);
    }

    @Transactional
    @CacheEvict(cacheNames = "movies-list", allEntries = true)
    public VideoDTO newMovie(VideoDTO movie) {
        Video newVideo = convertToEntity(movie);
        VideoDTO movieFound = convertToDto(videoRepository.save(newVideo));
        return movieFound;
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "movies-list")
    public List<VideoDTO> listAllMovies() {
        List<Video> moviesFoundList = videoRepository.findAll();
        List<VideoDTO> moviesListConverted = moviesFoundList.stream().map(this::convertToDto).collect(Collectors.toList());
        return moviesListConverted;
    }

    @Transactional
    @CachePut(cacheNames = "movies-list", key = "#movieRid")
    public VideoDTO updateMovie(UUID movieRid, VideoDTO movieRequest) {
        Video video = getMovieByRID(movieRid);
        video.setTitle(movieRequest.getTitle());
        video.setSynopsis(movieRequest.getSynopsis());
        video.setReleased(movieRequest.getReleased());
        video.setBackgroundImgUrl(movieRequest.getBackgroundImgUrl());
        video.setCoverImgUrl(movieRequest.getCoverImgUrl());
        video.setTrailerUrl(movieRequest.getTrailerUrl());
        videoRepository.save(video);
        return convertToDto(video);
    }

    @Transactional
    @CacheEvict(cacheNames = "movies-list", allEntries = true, key = "#movieRid")
    public void deleteMovie(UUID movieRid) {
        Video video = getMovieByRID(movieRid);
        videoRepository.delete(video);
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "movie-by-rid", key = "#rid")
    public Video getMovieByRID(UUID rid) {
        Video video = videoRepository.getMoviesByRidIs(rid).orElseThrow(() -> new ResourceNotFoundException("Video with " + rid + " not found"));
        return video;
    }

}

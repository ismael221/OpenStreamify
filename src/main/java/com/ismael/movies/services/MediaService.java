package com.ismael.movies.services;

import com.ismael.movies.model.Media;
import com.ismael.movies.repository.MediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MediaService {
    @Autowired
    MediaRepository mediaRepository;

    public Media mediaUpload(Media filme){
        return  mediaRepository.save(filme);
    }

    public Media retrieveMedia(UUID uuid){
        Media mediaFound = mediaRepository.findById(uuid).orElseThrow();
        return mediaFound;
    }
}

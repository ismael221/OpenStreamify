package com.ismael.movies.services;

import com.ismael.movies.model.Genre;
import com.ismael.movies.repository.GenreRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GenreService {

    final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public Genre createNewGenre(Genre genre){
        genre.setCreatedAt(Date.from(Instant.now()));
        genre.setUpdatedAt(Date.from(Instant.now()));
        return genreRepository.save(genre);
    }

    public Optional<Genre> retrieveGenreById(UUID rid){
        return genreRepository.findByRid(rid);
    }

    public List<Genre> listAllGenres(){
        return genreRepository.findAll();
    }

    public Set<Genre> findAllById(List<UUID> genres) {
        return genreRepository.findAllById(genres).stream().collect(Collectors.toSet());
    }
}

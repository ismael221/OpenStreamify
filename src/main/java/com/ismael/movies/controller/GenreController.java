package com.ismael.movies.controller;

import com.ismael.movies.enums.MovieGenre;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/genres")
public class GenreController {
    @GetMapping("/movie-genres")
    public List<String> getMovieGenres() {
        // Convert enum values into a list of strings
        return Arrays.stream(MovieGenre.values())
                .map(Enum::name) //Gets the name of each enum
                .collect(Collectors.toList());
    }

    //TODO fix to list the genre of only the specified film
    @GetMapping
    public List<String> getMovieGenreEqualsTo(String genre) {
        // Convert enum values into a list of strings
        return Arrays.stream(MovieGenre.values())
                .map(Enum::name) // Gets the name of each enum
                .collect(Collectors.toList());
    }
}

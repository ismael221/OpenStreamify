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
        // Converte os valores do enum em uma lista de strings
        return Arrays.stream(MovieGenre.values())
                .map(Enum::name) // Obtém o nome de cada enum
                .collect(Collectors.toList());
    }
}

package com.ismael.openstreamify.controller;

import com.ismael.openstreamify.model.Genre;
import com.ismael.openstreamify.services.GenreService;
import com.ismael.openstreamify.DTO.GenreDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@RestController
@RequestMapping("api/v1/genres")
public class GenreController {

    private final GenreService genreService;


    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @PostMapping
    public ResponseEntity<GenreDTO> createNewGenre(@RequestBody Genre genre) {
        Genre saved = genreService.createNewGenre(genre);
        GenreDTO dto = GenreDTO.convertToDTO(saved);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @GetMapping("/{rid}")
    public ResponseEntity<GenreDTO> getGenreByRid(@PathVariable UUID rid){
        Genre found = genreService.retrieveGenreById(rid).orElseThrow();
        GenreDTO dto = GenreDTO.convertToDTO(found);
        return new ResponseEntity<>(dto,HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<GenreDTO>> findAll(){
        List<Genre> list = genreService.listAllGenres();
         List<GenreDTO> newList = list.stream().map(GenreDTO::convertToDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(newList,HttpStatus.OK);
    }

}

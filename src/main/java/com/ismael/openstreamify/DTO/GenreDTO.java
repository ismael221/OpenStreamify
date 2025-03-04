package com.ismael.openstreamify.DTO;

import com.ismael.openstreamify.model.Genre;
import com.ismael.openstreamify.model.Movie;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GenreDTO {

    private UUID rid;
    private String name;
    private Date createdAt;
    private Date updatedAt;
    private Set<UUID> movieIds;

    public static GenreDTO convertToDTO(Genre genre) {
        GenreDTO genreDTO = new GenreDTO();
        genreDTO.setRid(genre.getRid());
        genreDTO.setName(genre.getName());
        genreDTO.setCreatedAt(genre.getCreatedAt());
        genreDTO.setUpdatedAt(genre.getUpdatedAt());

        // Mapeando apenas os IDs dos filmes
        Set<UUID> movieIds = genre.getMovies().stream()
                .map(Movie::getRid) // Assumindo que Movie também tem um campo rid
                .collect(Collectors.toSet());
        genreDTO.setMovieIds(movieIds);

        return genreDTO;
    }

}

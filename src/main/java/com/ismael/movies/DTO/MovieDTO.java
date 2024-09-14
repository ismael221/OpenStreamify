package com.ismael.movies.DTO;


import com.ismael.movies.enums.MovieGenre;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieDTO implements Serializable {
    @NotNull(message = "Movie title cannot be null")
    private String title;
    @NotNull(message = "Movie synopsis cannot be null")
    private String synopsis;
    @NotNull(message = "Movie genres cannot be null")
    private Set<MovieGenre> genres;
    private Date released;
    private UUID rid;
    private String backgroundImgUrl;
    private String coverImgUrl;
    private String trailerUrl;
    private String type;
}

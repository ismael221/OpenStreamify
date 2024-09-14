package com.ismael.movies.DTO;


import com.ismael.movies.enums.MovieGenre;
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
    private String title;
    private String synopsis;
    private Set<MovieGenre> genres;
    private Date released;
    private UUID rid;
    private String backgroundImgUrl;
    private String coverImgUrl;
    private String trailerUrl;
    private String type;
}

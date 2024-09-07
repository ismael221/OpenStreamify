package com.ismael.movies.DTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieDTO {
    private String title;
    private String synopsis;
    private String genre;
    private Date released;
    private UUID rid;
    private String backgroundImgUrl;
    private String coverImgUrl;
    private String trailerUrl;
    private String type;
}

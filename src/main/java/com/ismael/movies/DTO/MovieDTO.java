package com.ismael.movies.DTO;


import java.util.Date;
import java.util.List;
import java.util.UUID;

public class MovieDTO {
    private String title;
    private String synopsis;
    private String genre;
    private Date released;
    private UUID rid;
    private List<RatingDTO> review;
    private String backgroundImgUrl;
    private String coverImgUrl;
    private String trailerUrl;
    private String type;
}

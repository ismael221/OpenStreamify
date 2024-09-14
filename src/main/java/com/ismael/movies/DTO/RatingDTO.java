package com.ismael.movies.DTO;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;

public class RatingDTO implements Serializable {
    @NotNull(message = "Review's comment cannot be empty")
    private String comment;
    @NotNull(message = "The rating value cannot be empty")
    private int rating;
    private UUID rid;
}

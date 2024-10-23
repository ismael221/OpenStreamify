package com.ismael.movies.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RatingDTO implements Serializable {
    @NotNull(message = "Review's comment cannot be empty")
    private String comment;
    @NotNull(message = "The rating value cannot be empty")
    private int rating;
    private UUID rid;
    private UUID movie;
    private Date createAt;
    private String user;
}

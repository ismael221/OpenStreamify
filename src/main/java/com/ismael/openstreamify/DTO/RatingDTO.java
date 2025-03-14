package com.ismael.openstreamify.DTO;

import com.ismael.openstreamify.model.Rating;
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
    private Date createdAt;
    private String user;

    public static RatingDTO from(Rating rating) {
        RatingDTO ratingDTO = RatingDTO.builder().user(rating.getUser())
                .comment(rating.getComment())
                .createdAt(rating.getCreatedAt())
                .rating(rating.getRating())
                .rid(rating.getRid())
                .movie(rating.getVideo().getId())
                .build();
        return ratingDTO;
    }
}

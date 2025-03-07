package com.ismael.openstreamify.DTO;


import com.ismael.openstreamify.enums.VideoType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VideoDTO implements Serializable {
    @NotNull(message = "Video title cannot be null")
    private String title;
    @NotNull(message = "Video synopsis cannot be null")
    private String synopsis;
    @NotNull(message = "Video genres cannot be null")
    private List<UUID> genres;
    private Date released;
    private UUID rid;
    private String backgroundImgUrl;
    private String coverImgUrl;
    private String trailerUrl;
    private VideoType videoType;
    private Date releaseDate;
    private Date endDate;
}

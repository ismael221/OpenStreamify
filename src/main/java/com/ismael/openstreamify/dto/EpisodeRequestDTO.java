package com.ismael.openstreamify.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EpisodeRequestDTO {

    private String title;
    private int season;
    private int episodeNumber;
    private String coverImg;
    private String details;
}

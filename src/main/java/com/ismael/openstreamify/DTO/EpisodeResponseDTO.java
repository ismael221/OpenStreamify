package com.ismael.openstreamify.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EpisodeResponseDTO {
    private UUID id;
    private String title;
    private int season;
    private int episodeNumber;
    private String coverImg;
    private String details;
}

package com.ismael.openstreamify.DTO;

import com.ismael.openstreamify.enums.VideoType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SerieResponseDTO {

    private String title;
    private String synopsis;
    private Date released;
    private UUID id;
    private String backgroundImgUrl;
    private String coverImgUrl;
    private String trailerUrl;
    private VideoType videoType;
    private List<EpisodeResponseDTO> episodes;

}

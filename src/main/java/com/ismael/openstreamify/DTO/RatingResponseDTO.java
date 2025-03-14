package com.ismael.openstreamify.DTO;

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
public class RatingResponseDTO implements Serializable {
    private UUID rid;
    private String comment;
    private int rating;
    private String user;
    private Date createdAt;
}

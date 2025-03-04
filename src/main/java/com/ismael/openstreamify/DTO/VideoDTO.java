package com.ismael.openstreamify.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoDTO implements Serializable {
    private String tempDir;
    private UUID ridFilme;
    private Date date = new Date();
}

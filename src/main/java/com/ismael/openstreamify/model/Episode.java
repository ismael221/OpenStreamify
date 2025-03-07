package com.ismael.openstreamify.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;
import java.util.UUID;

@Data
@Entity
@Table(name = "episodes")
public class Episode {

    @Id
    @UuidGenerator
    @Column(unique = true,nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "series_id", nullable = false)
    private Video series;

    private String title;
    private int season;
    private int episodeNumber;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date createdAt;
}
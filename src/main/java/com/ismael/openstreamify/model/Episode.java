package com.ismael.openstreamify.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "episodes")
@Builder
public class Episode {

    @Id
    @UuidGenerator
    @Column(unique = true, nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "series_id", nullable = false)
    private Video series;

    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private int season;
    @Column(nullable = false)
    private int episodeNumber;
    @Column(nullable = false)
    private String coverImg;
    @Column(nullable = false)
    private String details;


    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date createdAt = new Date();
}
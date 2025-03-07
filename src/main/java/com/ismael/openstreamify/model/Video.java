package com.ismael.openstreamify.model;

import com.ismael.openstreamify.enums.VideoType;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "videos", uniqueConstraints = {
        @UniqueConstraint(columnNames = "rid")
})
public class Video implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String synopsis;

    private Date released;

    @UuidGenerator
    @Column(name = "rid", nullable = false, updatable = false, unique = true)
    private UUID rid;

    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    private List<Rating> review;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VideoType videoType;

    private Date releaseDate;
    private Date endDate;

    @ManyToMany
    @JoinTable(
            name = "MovieAndSeriesGenre",
            joinColumns = @JoinColumn(name = "movie_rid"),
            inverseJoinColumns = @JoinColumn(name = "genre_rid")
    )
    private Set<Genre> genres;

    private String backgroundImgUrl;
    private String coverImgUrl;
    private String trailerUrl;
    private String type;

    @OneToMany(mappedBy = "series", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Episode> episodes;
}


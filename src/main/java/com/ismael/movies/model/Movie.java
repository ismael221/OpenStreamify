package com.ismael.movies.model;


import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.*;

import java.sql.Types;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "movies")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String title;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String synopsis;
    private String genre;
    private Date released;
    @UuidGenerator
    @JdbcTypeCode(Types.VARCHAR)
    private UUID rid;
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    private List<Rating> review;
    private String backgroundImgUrl;
    private String coverImgUrl;
    private String trailerUrl;
    private String type;
}

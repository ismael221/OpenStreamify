package com.ismael.movies.model;


import com.ismael.movies.enums.MovieGenre;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.*;

import java.sql.Types;
import java.util.Date;
import java.util.List;
import java.util.Set;
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
    @ElementCollection(targetClass = MovieGenre.class) // Indica uma coleção de enumerações
    @Enumerated(EnumType.STRING) // Armazena os enums como strings no banco
    @CollectionTable(name = "movie_genres", joinColumns = @JoinColumn(name = "movie_id"))
    @Column(name = "genre")
    private Set<MovieGenre> genres;
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

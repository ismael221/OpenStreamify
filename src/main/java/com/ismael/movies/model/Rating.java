package com.ismael.movies.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;

import java.sql.Types;
import java.util.UUID;

@Data
@Entity
@Table(name = "ratings")
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String comment;
    private int rating;
    @UuidGenerator
    @JdbcTypeCode(Types.VARCHAR)
    private UUID rid;

}

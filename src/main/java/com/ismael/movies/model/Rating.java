package com.ismael.movies.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;
import java.sql.Types;
import java.util.UUID;

@Data
@Entity
@Table(name = "ratings",uniqueConstraints = {
        @UniqueConstraint(columnNames = "RID")
})
public class Rating implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private long id;
    @ManyToOne
    @JoinColumn(name = "movie_rid",nullable = false,referencedColumnName = "rid")
    private Movie movie;
    @Column(nullable = false,columnDefinition = "MEDIUMTEXT")
    private String comment;
    @Column(nullable = false)
    private int rating;
    @UuidGenerator
    @JdbcTypeCode(Types.VARCHAR)
    @Column(name = "rid",nullable = false)
    private UUID rid;

}

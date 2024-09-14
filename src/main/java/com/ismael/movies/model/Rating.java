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
@Table(name = "ratings",uniqueConstraints = {
        @UniqueConstraint(columnNames = "RID")
})
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @ManyToOne
    @JoinColumn(name = "movie_rid",referencedColumnName = "rid")
    private Movie movie;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String comment;
    private int rating;
    @UuidGenerator
    @JdbcTypeCode(Types.VARCHAR)
    @Column(name = "rid",nullable = false)
    private UUID rid;

}

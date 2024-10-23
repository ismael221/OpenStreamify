package com.ismael.movies.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;
import java.sql.Types;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
@Table(name = "ratings",uniqueConstraints = {
        @UniqueConstraint(columnNames = "RID")
})
public class Rating implements Serializable {
    @Id
    @UuidGenerator
    @JdbcTypeCode(Types.VARCHAR)
    @Column(unique = true,nullable = false)
    private UUID rid;
    @ManyToOne
    @JoinColumn(name = "movie_rid",nullable = false,referencedColumnName = "rid")
    private Movie movie;
    @Column(nullable = false,columnDefinition = "MEDIUMTEXT")
    private String comment;
    @Column(nullable = false)
    private int rating;

    private Date createdAt;
    private String user;

}

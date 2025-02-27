package com.ismael.movies.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;

import java.sql.Types;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
public class Genre {

    @Id
    @UuidGenerator
    @JdbcTypeCode(Types.VARCHAR)
    @Column(name = "rid",nullable = false)
    private UUID rid;
    private String name;
    private Date createdAt;
    private Date updatedAt;
    @ManyToMany(mappedBy = "genres")
    private Set<Movie> movies;

}

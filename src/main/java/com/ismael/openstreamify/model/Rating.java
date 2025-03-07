package com.ismael.openstreamify.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
@Table(name = "ratings", uniqueConstraints = {
        @UniqueConstraint(columnNames = "rid")
})
public class Rating implements Serializable {

    @Id
    @UuidGenerator
    @Column(unique = true, nullable = false, updatable = false)
    private UUID rid;

    @ManyToOne
    @JoinColumn(name = "movie_rid", nullable = false)
    @JsonBackReference
    private Video video;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String comment;

    @Column(nullable = false)
    private int rating;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date createdAt;

    @Column(name = "username")
    private String user;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }
}
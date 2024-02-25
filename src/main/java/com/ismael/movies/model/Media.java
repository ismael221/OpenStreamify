package com.ismael.movies.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Data
@Entity
public class Media {
    @Id
    @UuidGenerator
    private UUID rid;
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] data;
    @OneToOne
    @JoinColumn(name = "id_filme")
    @JsonBackReference
    private Filme filme;
}

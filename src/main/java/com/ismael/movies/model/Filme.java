package com.ismael.movies.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "filmes")
public class Filme {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String titulo;
    private String sinopse;
    private String genero;
    private String anoLancamento;
    @UuidGenerator
    private UUID rid;
    @OneToMany(mappedBy = "filme", cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    private List<Analise> analises;
    public Filme() {
    }
}

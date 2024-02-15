package com.ismael.movies.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "analises")
public class Analise {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "id_filme")
    @JsonBackReference
    private Filme filme;

    private String comment;
    private int nota;

    public Analise() {
    }


}

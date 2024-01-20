package com.ismael.movies.model;

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
    private Filme filme;

    private String comment;
    private int nota;

    public Analise() {
    }


}

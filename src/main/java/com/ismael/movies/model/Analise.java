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
    private String filme;
    private String analise;
    private int nota;

    public Analise() {
    }

    public Analise(long id, String filme, String analise, int nota) {
        this.id = id;
        this.filme = filme;
        this.analise = analise;
        this.nota = nota;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFilme() {
        return filme;
    }

    public void setFilme(String filme) {
        this.filme = filme;
    }

    public String getAnalise() {
        return analise;
    }

    public void setAnalise(String analise) {
        this.analise = analise;
    }

    public int getNota() {
        return nota;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }
}

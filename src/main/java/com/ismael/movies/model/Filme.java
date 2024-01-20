package com.ismael.movies.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

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
    private String  anoLancamento;

    public Filme() {
    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSinopse() {
        return sinopse;
    }

    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getAnoLancamento() {
        return anoLancamento;
    }

    public void setAnoLancamento(String anoLancamento) {
        this.anoLancamento = anoLancamento;
    }
}

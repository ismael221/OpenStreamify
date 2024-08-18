package com.ismael.movies.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.*;

import java.sql.Types;
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
    @Column(columnDefinition = "MEDIUMTEXT")
    private String sinopse;
    private String genero;
    private String anoLancamento;
    @UuidGenerator
   // @JdbcTypeCode(Types.VARCHAR)
    private UUID rid;
    @OneToMany(mappedBy = "filme", cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    private List<Analise> analises;
}

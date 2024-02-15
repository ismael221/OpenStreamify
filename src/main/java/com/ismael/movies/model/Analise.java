package com.ismael.movies.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.util.QTypeContributor;

import java.sql.Types;
import java.util.UUID;

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
    @Column(columnDefinition = "MEDIUMTEXT")
    private String comment;
    private int nota;
    @UuidGenerator
    @JdbcTypeCode(Types.VARCHAR)
    private UUID rid;

}

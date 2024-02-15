package com.ismael.movies.repository;

import com.ismael.movies.model.Analise;
import com.ismael.movies.model.Filme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilmeRepository extends JpaRepository<Filme,Integer> {
}

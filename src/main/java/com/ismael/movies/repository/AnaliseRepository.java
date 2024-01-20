package com.ismael.movies.repository;

import com.ismael.movies.model.Analise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnaliseRepository extends JpaRepository<Analise,Integer> {
    List<Analise> findByFilme_Id(Integer id);
}

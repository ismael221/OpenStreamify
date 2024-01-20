package com.ismael.movies.service;

import com.ismael.movies.model.Analise;
import com.ismael.movies.model.Filme;
import com.ismael.movies.repository.AnaliseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnaliseService {

    @Autowired
    AnaliseRepository analiseRepository;

    public Analise adicionarAnalise(Analise analise){
            analiseRepository.save(analise);
            return analise;
    }

    public List<Analise> listarAnalises(Integer filmeId){
         List<Analise> comentarios =  analiseRepository.findByFilme_Id(filmeId);
         return comentarios;
    }
}

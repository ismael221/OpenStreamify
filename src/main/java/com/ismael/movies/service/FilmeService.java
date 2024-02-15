package com.ismael.movies.service;

import com.ismael.movies.model.Analise;
import com.ismael.movies.model.Filme;
import com.ismael.movies.repository.FilmeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FilmeService {
    @Autowired
    FilmeRepository filmeRepository;


    public Filme cadastrarFilme(Filme filme){
        filmeRepository.save(filme);
        return filme;
    }

    public List<Filme> listaFilmes(){
        return filmeRepository.findAll();
    }

    public Filme getFilmePorID(Integer filmeID){
        return filmeRepository.findById(filmeID).orElseThrow();
    }

    public  Filme atualizarFilme(Integer filmeID,Filme filmeRequest){
            Filme filme = getFilmePorID(filmeID);
            filme.setGenero(filmeRequest.getGenero());
            filme.setTitulo(filmeRequest.getTitulo());
            filme.setSinopse(filmeRequest.getSinopse());
            filme.setAnoLancamento(filmeRequest.getAnoLancamento());
            filmeRepository.save(filme);
            return filme;
    }

    public void deletarFilme(Integer filmeID){
            Filme filme = getFilmePorID(filmeID);
            filmeRepository.deleteById((int) filme.getId());
    }
}

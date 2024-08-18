package com.ismael.movies.services;

import com.ismael.movies.model.Filme;
import com.ismael.movies.repository.FilmeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class FilmeService {
    @Autowired
    FilmeRepository filmeRepository;

    private  static  final Logger logger = LoggerFactory.getLogger(FilmeService.class);

    public Filme cadastrarFilme(Filme filme){
        filmeRepository.save(filme);

        return filme;
    }

    public List<Filme> listaFilmes(){
        logger.info("Informação básica");
        logger.debug("Informação detalhada para debug");
        logger.error("Algo deu errado!");
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

    public Filme getFilmePorRid(UUID rid){
        Filme filme = filmeRepository.getFilmesByRidIs(rid);
        return filme;
    }

}

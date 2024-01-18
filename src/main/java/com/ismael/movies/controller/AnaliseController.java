package com.ismael.movies.controller;

import com.ismael.movies.model.Analise;
import com.ismael.movies.model.Filme;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AnaliseController {

    private List<Analise> analises = new ArrayList();

    @PostMapping("/adicionarAnalise")
    public String adicionarAnalise(@ModelAttribute Analise analise, @ModelAttribute Filme filme){
        
        return "redirect:/listarFilmes";
    }
}

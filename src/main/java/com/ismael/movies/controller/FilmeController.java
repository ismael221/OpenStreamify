package com.ismael.movies.controller;

import com.ismael.movies.model.Analise;
import com.ismael.movies.model.Filme;
import com.ismael.movies.service.AnaliseService;
import com.ismael.movies.service.FilmeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
public class FilmeController {

        @Autowired
        FilmeService filmeService;

        @Autowired
        AnaliseService analiseService;

        @GetMapping("/")
        public String homePage(){
            return "index";
        }

        @GetMapping("/cadastrarFilme")
        public  String mostrarFormCadastro(Model model){
                model.addAttribute("filme",new Filme());
                return "cadastrar";
        }

        @PostMapping("/cadastrarFilme")
        public String cadastrarFilme(@ModelAttribute Filme filme) {
                filmeService.cadastrarFilme(filme);
                return "redirect:/listarFilmes";
        }

        @GetMapping("/listarFilmes")
        public String listarFilmes(Model model){
                model.addAttribute("filmes",filmeService.listaFilmes());
                return "filmes";
        }

        @GetMapping("/exibirAnalise/{id}")
        public String analisePorFilme(Model model, @PathVariable Integer id)
        {
                Filme filmeEncontrado = filmeService.getFilmePorID(id);
                List<Analise> analisesEcontradas = analiseService.listarAnalises(id);
                model.addAttribute("filme",filmeEncontrado);
                model.addAttribute("feedback", new Analise());
                model.addAttribute("analises", analisesEcontradas);
                return  "detalhes";
        }

        @PostMapping("/cadastrarAnalise")
        public String cadastrarAnalise(@ModelAttribute Analise analise,@ModelAttribute Filme filme,Model model) {
                analise.setFilme(filme);
                analiseService.adicionarAnalise(analise);
                return "redirect:/listarFilmes";
        }


}

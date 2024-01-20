package com.ismael.movies.controller;

import com.ismael.movies.model.Analise;
import com.ismael.movies.model.Filme;
import com.ismael.movies.service.FilmeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.model.IModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class FilmeController {

        @Autowired
        FilmeService filmeService;

        private List<Filme> filmes = filmeService.listaFilmes();
        private  List<Analise> analises = new ArrayList<>();

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
                model.addAttribute("filmes",filmes);
                return "filmes";
        }

        @GetMapping("/exibirAnalise")
        public String analisePorFilme(Model model, @RequestParam String id)
        {
                Integer idFilme = Integer.parseInt(id);
                Filme filmeEncontrado = new Filme();
                for (Filme f:filmes){
                        if (f.getId() == idFilme){
                                filmeEncontrado = f;
                                break;
                        }
                }

                List<Analise> analisesEcontradas = new ArrayList<>();
                for (Analise a:analises){
                        if (a.getId() == idFilme){
                                analisesEcontradas.add(a);
                                break;
                        }
                }
                model.addAttribute("filme",filmeEncontrado );
                model.addAttribute("analise", new Analise());
                model.addAttribute("analises", analisesEcontradas);
                return  "detalhes";
        }

        @PostMapping("/cadastrarAnalise")
        public String cadastrarAnalise(@ModelAttribute Analise analise,@ModelAttribute Filme filme,Model model) {
                analise.setId(filme.getId());
                analise.setFilme(filme.getTitulo());
                analises.add(analise);
                return "redirect:/listarFilmes";
        }


}

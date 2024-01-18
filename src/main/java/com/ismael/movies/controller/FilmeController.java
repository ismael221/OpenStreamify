package com.ismael.movies.controller;

import com.ismael.movies.model.Filme;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.thymeleaf.model.IModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class FilmeController {

        private List<Filme> filmes= new ArrayList();

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
        public String cadastrarFilme(@ModelAttribute Filme filme) throws ParseException {
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                Date data = format.parse(filme.getAnoLancamento());
                filmes.add(filme);
                System.out.println(filme);
                return "index";
        }

        @GetMapping("/listarFilmes")
        public String listarFilmes(Model model){
                model.addAttribute("filmes",filmes);
                return "filmes";
        }

        @GetMapping("/detalhes/{id}")
        public String detalhar(@PathVariable long id,Model model)
        {
                for (Filme filme:filmes){
                        if (filme.getId() == id){
                                model.addAttribute("filme",filme);
                                return "detalhes";
                        }
                }
                return  null;
        }

}

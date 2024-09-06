package com.ismael.movies.controller;

import com.ismael.movies.cookies.model.Preferencia;
import com.ismael.movies.model.Movie;
import com.ismael.movies.model.Rating;
import com.ismael.movies.services.RatingService;
import com.ismael.movies.services.MoviesService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class MovieController {
        public String theme ="light";
        final
        MoviesService filmeService;

        @Autowired
        RatingService ratingService;

        public MovieController(MoviesService filmeService) {
                this.filmeService = filmeService;
        }

        @GetMapping("/auth/login")
        public String loginPage(){
                return "login";
        }
        @GetMapping("/")
        public String homePage(@CookieValue(name="pref-nome", defaultValue="") String style, @CookieValue(name="pref-estilo", defaultValue="light")String tema, Model model){
                theme = tema;
                model.addAttribute("css", tema);
                model.addAttribute("style", style);
                return "index";
        }

        @GetMapping("/cadastrarFilme")
        public  String mostrarFormCadastro(Model model){
                model.addAttribute("css",theme);
                model.addAttribute("filme",new Movie());
                return "cadastrar";
        }

        @PostMapping("/cadastrarFilme")
        public String cadastrarFilme(@ModelAttribute Movie movie) {
                filmeService.addMovie(movie);
                return "redirect:/listarFilmes";
        }

        @GetMapping("/listarFilmes")
        public String listarFilmes(Model model){
                model.addAttribute("css",theme);
                model.addAttribute("filmes",filmeService.listAllMovies());
                return "filmes";
        }

        @GetMapping("/exibirAnalise/{id}")
        public String analisePorFilme(Model model, @PathVariable Integer id)
        {
                Movie movieEncontrado = filmeService.getMovieById(id);
                List<Rating> analisesEcontradas = ratingService.listRatingsByMovieId(id);
                model.addAttribute("filme", movieEncontrado);
                model.addAttribute("feedback", new Rating());
                model.addAttribute("analises", analisesEcontradas);
                model.addAttribute("css",theme);
                return  "detalhes";
        }

        @PostMapping("/cadastrarAnalise")
        public String cadastrarAnalise(@ModelAttribute Rating analise, @ModelAttribute Movie movie, Model model) {
                analise.setMovie(movie);
                ratingService.addRating(analise);
                return "redirect:/listarFilmes";
        }

        @PostMapping("/preferencias")
        public ModelAndView gravaPreferencias(@ModelAttribute Preferencia pref, HttpServletResponse response){
         Cookie cookiePrefNome = new Cookie("pref-nome", "style");
         cookiePrefNome.setDomain("localhost"); //disponível apenas no domínio "localhost"
         cookiePrefNome.setHttpOnly(true); //acessível apenas por HTTP, JS não
         cookiePrefNome.setMaxAge(86400); //1 dia
         response.addCookie(cookiePrefNome);
         Cookie cookiePrefEstilo = new Cookie("pref-estilo", pref.getEstilo());
         cookiePrefEstilo.setDomain("localhost"); //disponível apenas no domínio "localhost"
         cookiePrefEstilo.setHttpOnly(true); //acessível apenas por HTTP, JS não
         cookiePrefEstilo.setMaxAge(86400); //1 dia
         response.addCookie(cookiePrefEstilo);
         return new ModelAndView("redirect:/"); //"index";

        }

        @GetMapping("/assistir")
        public String assistirFilme(@RequestParam String filme, Model model) {
                model.addAttribute("filme", filme);
                return "assistir";  // Nome do template Thymeleaf
        }
        //TODO Adicionar o enpoint para redirecionar para os detalhes do filme com o botão de play antes de reproduzir diretamente.

        @GetMapping("/detalhes")
        public String detalhaFilme(){
                return "detalhes";
        }
}

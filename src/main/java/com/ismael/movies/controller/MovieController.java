package com.ismael.movies.controller;

import com.ismael.movies.DTO.MovieDTO;
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

import java.util.ArrayList;
import java.util.List;

@Controller
public class MovieController {
        public String theme ="light";
        final
        MoviesService moviesService;

        @Autowired
        RatingService ratingService;

        public MovieController(MoviesService moviesService) {
                this.moviesService = moviesService;
        }

        @GetMapping("/auth/login")
        public String loginPage(){
                return "login";
        }


        @GetMapping("/")
        public String homePage(Model model) {
                List<MovieDTO> moviesList = moviesService.listAllMovies();

                // Particionar a lista de filmes manualmente
                List<List<MovieDTO>> movieChunks = partitionList(moviesList, 4);
                model.addAttribute("moviesChunks", movieChunks);
                return "index";
        }

        // Método auxiliar para particionar a lista
        private <T> List<List<T>> partitionList(List<T> list, int chunkSize) {
                List<List<T>> partitions = new ArrayList<>();
                for (int i = 0; i < list.size(); i += chunkSize) {
                        partitions.add(list.subList(i, Math.min(i + chunkSize, list.size())));
                }
                return partitions;
        }

        @GetMapping("/cadastrarFilme")
        public  String mostrarFormCadastro(Model model){
                model.addAttribute("css",theme);
                model.addAttribute("filme",new Movie());
                return "cadastrar";
        }

        @PostMapping("/cadastrarFilme")
        public String cadastrarFilme(@ModelAttribute MovieDTO movie) {
                moviesService.addMovie(movie);
                return "redirect:/listarFilmes";
        }

        @GetMapping("/listarFilmes")
        public String listarFilmes(Model model){
             //   model.addAttribute("css",theme);
                model.addAttribute("filmes",moviesService.listAllMovies());
                return "filmes";
        }

        @GetMapping("/exibirAnalise/{id}")
        public String analisePorFilme(Model model, @PathVariable Integer id)
        {
                Movie movieEncontrado = moviesService.getMovieById(id);
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

        @GetMapping("/play")
        public String assistirFilme(@RequestParam String movie, Model model) {
                model.addAttribute("movie", movie);
                return "assistir";  // Nome do template Thymeleaf
        }
        //TODO Adicionar o enpoint para redirecionar para os detalhes do filme com o botão de play antes de reproduzir diretamente.

        @GetMapping("/detalhes")
        public String detalhaFilme(){
                return "detalhes";
        }

        //TODO adicionar enpointe para listar as series,baseado no type da entity movies
}

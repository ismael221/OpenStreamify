package com.ismael.movies.controller;

import com.ismael.movies.DTO.MovieDTO;
import com.ismael.movies.cookies.model.Preferencia;
import com.ismael.movies.enums.MovieGenre;
import com.ismael.movies.model.Movie;
import com.ismael.movies.model.Rating;
import com.ismael.movies.services.RatingService;
import com.ismael.movies.services.MoviesService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MovieController {
        public String theme ="light";
        final
        MoviesService moviesService;

        @Autowired
        RatingService ratingService;
        @Autowired
        ConfigRestController config;

        public MovieController(MoviesService moviesService) {
                this.moviesService = moviesService;
        }

        //TODO Add the logic to sign in the user
        @GetMapping("/auth/login")
        public String loginPage(){
                return "login";
        }

        @GetMapping("/logout")
        public String signInPage(HttpServletRequest request, HttpServletResponse response){
                // Remove o cookie
                var cookies = request.getCookies();
                if (cookies != null) {
                        for (var cookie : cookies) {
                                if ("access_token".equals(cookie.getName())) {
                                        cookie.setValue(null);
                                        cookie.setMaxAge(0); // Define o tempo de vida do cookie para 0
                                        cookie.setPath("/"); // Certifique-se de que o caminho está correto
                                        response.addCookie(cookie);
                                }
                        }
                }
                return "redirect:/auth/login";
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
        public String analisePorFilme(Model model, @PathVariable UUID rid)
        {
                Movie movieEncontrado = moviesService.getMovieByRID(rid);
                List<Rating> analisesEcontradas = ratingService.listRatingsByMovieRID(rid);
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

        @GetMapping("/play/{rid}")
        public String assistirFilme(@PathVariable("rid") String mediaRID, Model model) {
                UUID uuid = UUID.fromString(mediaRID); // Verifica se é um UUID válido
                Movie media = moviesService.getMovieByRID(uuid);
                String serverUrl = config.getServerUrl();
                model.addAttribute("media", media);
                model.addAttribute("config",serverUrl);
                return "assistir";  // Nome do template Thymeleaf
        }
        //TODO Adicionar o enpoint para redirecionar para os detalhes do filme com o botão de play antes de reproduzir diretamente.

        @GetMapping("/detalhes/{rid}")
        public String detalhaFilme(@PathVariable("rid") String movie_RID, Model model){
                UUID uuid = UUID.fromString(movie_RID); // Verifica se é um UUID válido
                List<String> genres = Arrays.stream(MovieGenre.values())
                        .map(Enum::name)
                        .collect(Collectors.toList());
                model.addAttribute("genres", genres);
                Movie  movieDetails = moviesService.getMovieByRID(uuid);
                model.addAttribute("details",movieDetails);
                return "detalhes";
        }

        @GetMapping("/auth/register")
        public String registerUser(){
                return "register";
        }

        //TODO adicionar enpoint para listar as series,baseado no type da entity movies
}

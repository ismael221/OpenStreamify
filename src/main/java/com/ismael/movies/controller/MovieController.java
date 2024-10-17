package com.ismael.movies.controller;

import com.ismael.movies.DTO.MovieDTO;
import com.ismael.movies.DTO.RatingDTO;
import com.ismael.movies.cookies.model.Preferencia;
import com.ismael.movies.enums.MovieGenre;
import com.ismael.movies.infra.security.TokenService;
import com.ismael.movies.model.Movie;
import com.ismael.movies.model.Rating;
import com.ismael.movies.model.Users.User;
import com.ismael.movies.services.RatingService;
import com.ismael.movies.services.MoviesService;
import com.ismael.movies.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@CrossOrigin(origins = "*", allowedHeaders = "*")
@CacheConfig(cacheNames = "templates")
public class MovieController {
        public String theme ="light";
        final
        MoviesService moviesService;

        @Autowired
        RatingService ratingService;
        @Autowired
        ConfigRestController config;
        @Autowired
        TokenService tokenService;

        @Autowired
        UserService userService;

        public MovieController(MoviesService moviesService) {
                this.moviesService = moviesService;
        }


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
                                        cookie.setHttpOnly(false);

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
        @CachePut
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
                moviesService.newMovie(movie);
                return "redirect:/listarFilmes";
        }

        @GetMapping("/list")
        public String listarFilmes(Model model){
             //   model.addAttribute("css",theme);
                model.addAttribute("moviesList",moviesService.listAllMovies());
                return "movies";
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
                return  "details";
        }

        @PostMapping("/cadastrarAnalise")
        public String cadastrarAnalise(@ModelAttribute RatingDTO analise, @ModelAttribute Movie movie, Model model) {
                analise.setMovie(movie.getRid());
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

        @Cacheable
        @GetMapping("/play/{rid}")
        public String assistirFilme(@PathVariable("rid") String mediaRID, Model model) {
                UUID uuid = UUID.fromString(mediaRID); // Verifica se é um UUID válido
                Movie media = moviesService.getMovieByRID(uuid);
                String serverUrl = config.getServerUrl();
                model.addAttribute("media", media);
                model.addAttribute("config",serverUrl);
                return "assistir";  // Nome do template Thymeleaf
        }

        @Cacheable
        @GetMapping("/details/{rid}")
        public String detalhaFilme(@PathVariable("rid") String movie_RID, Model model){
                UUID uuid = UUID.fromString(movie_RID); // Verifica se é um UUID válido
                List<String> genres = Arrays.stream(MovieGenre.values())
                        .map(Enum::name)
                        .collect(Collectors.toList());
                model.addAttribute("genres", genres);
                Movie  movieDetails = moviesService.getMovieByRID(uuid);
                model.addAttribute("details",movieDetails);
                return "details";
        }

        @GetMapping("/auth/register")
        public String registerUser(){
                return "register";
        }


        @GetMapping("/auth/reset")
        public String resetPassword(){
                return "reset";
        }

        @GetMapping("/auth/user/{token}")
        public String successfullPasswordReset(@PathVariable("token") String request_Token, HttpServletRequest request, HttpServletResponse response){
                Logger logger = LoggerFactory.getLogger(MovieController.class);

                logger.info("Iniciando validação do token: " + request_Token);
                String validated = tokenService.validateToken(request_Token);
                logger.info("Token validado: " + validated);


                if (validated == null || validated.isEmpty()) {
                        logger.warn("Token inválido. Redirecionando para /auth/reset");
                        return "redirect:/auth/reset"; // Token inválido, redireciona
                }

                Cookie[] cookies = request.getCookies();
                if (cookies != null) {
                        for (Cookie cookie : cookies) {
                                if ("access_token".equals(cookie.getName())) {
                                        cookie.setValue(null);
                                        cookie.setHttpOnly(true);
                                        cookie.setMaxAge(0); // Expira imediatamente
                                        cookie.setPath("/");
                                        response.addCookie(cookie);
                                        logger.info("Cookie de 'access_token' removido.");
                                }
                        }
                }

                User usuario = userService.findUserByLogin(validated);
                if (usuario != null) {
                        logger.info("Usuário encontrado: " + usuario.getUsername());
                        Cookie newCookie = new Cookie("access_token", request_Token);
                        newCookie.setHttpOnly(true);
                        newCookie.setSecure(false);
                        newCookie.setPath("/");
                        response.addCookie(newCookie);
                        logger.info("Novo cookie 'access_token' adicionado.");
                        request.getSession().setAttribute("email", usuario.getLogin());
                        return "redirect:/auth/update";
                } else {
                        logger.warn("Usuário não encontrado para o token validado.");
                }

                logger.warn("Redirecionando para /auth/reset devido a token inválido ou usuário não encontrado.");
                return "redirect:/auth/reset";
        }

        @Cacheable
        @GetMapping("/auth/update")
        public String updatePassword(HttpServletRequest request, Model model) {
                String email = (String) request.getSession().getAttribute("email");

                if (email == null) {
                        return "redirect:/auth/reset";
                }
                model.addAttribute("email", email);
                return "updatePassword";
        }

        @GetMapping("/live")
        public String retrieveLiveStreaming(){
                return "live";
        }


        //TODO adicionar enpoint para listar as series,baseado no type da entity movies

        @GetMapping("/admin")
        public String adminPanel(){
                return "admin";
        }

        @GetMapping("auth/register-code")
        public String registerCodeInput(){
                return "register-code";
        }
}

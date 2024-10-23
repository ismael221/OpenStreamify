package com.ismael.movies.controller;

import com.ismael.movies.DTO.MovieDTO;
import com.ismael.movies.enums.MovieGenre;
import com.ismael.movies.infra.security.TokenService;
import com.ismael.movies.model.Movie;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.*;
import java.util.stream.Collectors;

@Controller
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MovieController {

        final
        MoviesService moviesService;

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

        private <T> List<List<T>> partitionList(List<T> list, int chunkSize) {
                List<List<T>> partitions = new ArrayList<>();
                for (int i = 0; i < list.size(); i += chunkSize) {
                        partitions.add(list.subList(i, Math.min(i + chunkSize, list.size())));
                }
                return partitions;
        }


        @GetMapping("/list")
        public String listarFilmes(Model model){
             //   model.addAttribute("css",theme);
                model.addAttribute("moviesList",moviesService.listAllMovies());
                return "movies";
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


        @GetMapping("/auth/update")
        public String updatePassword(HttpServletRequest request, Model model) {
                String email = (String) request.getSession().getAttribute("email");

                if (email == null) {
                        return "redirect:/auth/reset";
                }
                model.addAttribute("email", email);
                return "updatePassword";
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

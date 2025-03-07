package com.ismael.openstreamify.controller;

import com.ismael.openstreamify.DTO.VideoDTO;
import com.ismael.openstreamify.infra.security.TokenService;
import com.ismael.openstreamify.model.Video;
import com.ismael.openstreamify.model.Users.User;
import com.ismael.openstreamify.services.VideosService;
import com.ismael.openstreamify.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.*;

@Controller
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MovieController {

        final
        VideosService videosService;

        final
        TokenService tokenService;

        final
        UserService userService;

        @Value("${server.url}")
        private String serverUrl;

        public MovieController(VideosService videosService, TokenService tokenService, UserService userService) {
                this.videosService = videosService;
                this.tokenService = tokenService;
                this.userService = userService;
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
                                        cookie.setMaxAge(0); // Sets cookie lifetime to 0
                                        cookie.setPath("/");
                                        response.addCookie(cookie);
                                } else if ("JSESSIONID".equals(cookie.getName())) {
                                        cookie.setValue(null);
                                        cookie.setHttpOnly(false);
                                        cookie.setMaxAge(0);
                                        cookie.setPath("/");
                                        response.addCookie(cookie);
                                }
                        }
                }
                return "redirect:/auth/login";
        }


        @GetMapping("/")
        public String homePage(Model model) {

                List<VideoDTO> moviesList = videosService.listAllMovies();

                // Partition the movie list manually
                List<List<VideoDTO>> movieChunks = partitionList(moviesList, 4);
                model.addAttribute("moviesChunks", movieChunks);
                return "index";
        }

        // Helper method to partition the list

        private <T> List<List<T>> partitionList(List<T> list, int chunkSize) {
                List<List<T>> partitions = new ArrayList<>();
                for (int i = 0; i < list.size(); i += chunkSize) {
                        partitions.add(list.subList(i, Math.min(i + chunkSize, list.size())));
                }
                return partitions;
        }


        @GetMapping("/list")
        public String listAllMovies(Model model){
             //   model.addAttribute("css",theme);
                model.addAttribute("moviesList", videosService.listAllMovies());
                return "movies";
        }


        @GetMapping("/play/{rid}")
        public String watchMovie(@PathVariable("rid") String mediaRID, Model model) {
                UUID uuid = UUID.fromString(mediaRID); // Checks if it is a valid UUID
                Video media = videosService.getMovieByRID(uuid);
                String serverUrl = this.serverUrl;
                model.addAttribute("media", media);
                model.addAttribute("config",serverUrl);
                return "watch";  // Nome do template Thymeleaf
        }


        @GetMapping("/details/{rid}")
        public String detailMovie(@PathVariable("rid") String movie_RID, Model model){
                UUID uuid = UUID.fromString(movie_RID); // Checks if it is a valid UUID
//                List<String> genres = Arrays.stream(Genre.values())
//                        .map(Genre::getName)
//                        .collect(Collectors.toList());
//                genres.forEach(System.out::println);
//                model.addAttribute("genres", genres);
                Video videoDetails = videosService.getMovieByRID(uuid);
                model.addAttribute("details", videoDetails);
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

        //TODO add enpoint to list the series, based on the entity movies type

        @GetMapping("/admin")
        public String adminPanel(){
                return "admin";
        }

        @GetMapping("auth/register-code")
        public String registerCodeInput(){
                return "register-code";
        }
}

package com.ismael.movies.controller;

import com.ismael.movies.infra.security.TokenService;
import com.ismael.movies.model.Users.AuthenticationDTO;
import com.ismael.movies.model.Users.LoginResponseDTO;
import com.ismael.movies.model.Users.RegisterDTO;
import com.ismael.movies.model.Users.User;
import com.ismael.movies.repository.UserRepository;
import com.ismael.movies.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.cloudfoundry.Token;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthenticationRestController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    UserRepository userRepository;

    @Autowired
    TokenService tokenService;

    @Autowired
    UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Validated AuthenticationDTO data, HttpServletResponse response){
         var userNamePassword = new UsernamePasswordAuthenticationToken(data.login(),data.password());
         var auth = this.authenticationManager.authenticate(userNamePassword);
         var token = tokenService.generateToken((User) auth.getPrincipal());
        Cookie cookie = new Cookie("access_token", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // Enviar apenas por HTTPS
        cookie.setPath("/"); // Disponível para toda a aplicação
        response.addCookie(cookie);
         return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Validated RegisterDTO data){
        if (this.userRepository.findByLogin(data.login()) != null) return ResponseEntity.badRequest().build();

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        User newUser = new User(data.login(),encryptedPassword,data.role());
        this.userRepository.save(newUser);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/update")
    public ResponseEntity changePassword(@RequestBody @Validated RegisterDTO user){
        User user1 = userService.findUserByLogin(user.login());
        if (user1.getLogin() != null) {
            String encryptedPassword = new BCryptPasswordEncoder().encode(user.password());
            user1.setPassword(encryptedPassword);
            userService.updateUserLogin(user1);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.badRequest().build();
    }



}

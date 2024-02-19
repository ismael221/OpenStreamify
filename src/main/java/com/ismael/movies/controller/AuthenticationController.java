package com.ismael.movies.controller;

import com.ismael.movies.infra.security.TokenService;
import com.ismael.movies.model.User.AuthenticationDTO;
import com.ismael.movies.model.User.LoginResponseDTO;
import com.ismael.movies.model.User.RegisterDTO;
import com.ismael.movies.model.User.User;
import com.ismael.movies.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    UserRepository userRepository;

    @Autowired
    TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Validated AuthenticationDTO data){
         var userNamePassword = new UsernamePasswordAuthenticationToken(data.login(),data.password());
         var auth = this.authenticationManager.authenticate(userNamePassword);
         var token = tokenService.generateToken((User) auth.getPrincipal());
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
}

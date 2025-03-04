package com.ismael.openstreamify.controller;

import com.ismael.openstreamify.infra.security.TokenService;
import com.ismael.openstreamify.model.Users.*;
import com.ismael.openstreamify.repository.UserRepository;
import com.ismael.openstreamify.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthenticationRestController {

    private final AuthenticationManager authenticationManager;
    final
    UserRepository userRepository;

    final
    TokenService tokenService;

    final
    UserService userService;

    public AuthenticationRestController(AuthenticationManager authenticationManager, UserRepository userRepository, TokenService tokenService, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Validated AuthenticationDTO data, HttpServletResponse response){
        User userFound = userService.findUserByLogin(data.login());
        if (userFound.isActive()){
            var userNamePassword = new UsernamePasswordAuthenticationToken(data.login(),data.password());
            var auth = this.authenticationManager.authenticate(userNamePassword);
            var token = tokenService.generateToken((User) auth.getPrincipal());
            Cookie cookie = new Cookie("access_token", token);
            cookie.setHttpOnly(true);
            cookie.setSecure(false); // Send only via HTTPS
            cookie.setPath("/"); // Available for all applications
            response.addCookie(cookie);
            return ResponseEntity.ok(new LoginResponseDTO(token));
        }else {
            return ResponseEntity.badRequest().build();
        }

    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Validated RegisterDTO data){
        userService.createNewUser(data);
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

    @GetMapping("/{login}")
    public ResponseEntity<UUID> getUserUUID(@PathVariable String login){
        UUID userFound = userService.findUserIdByLogin(login);
        return new ResponseEntity<>(userFound, HttpStatus.OK);
    }

    @GetMapping("/oauth")
    public ResponseEntity<String> userLoggedWithOAuth2(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            CustomOAuth2User oauthUser = (CustomOAuth2User) auth.getPrincipal();
            String email = oauthUser.getEmail();
            System.out.println("E-mail do usuário: " + email);
            return new ResponseEntity<>(email,HttpStatus.OK);
        }

        return null;
    }

    @GetMapping("/bearer")
    public ResponseEntity<String> userLoggedWithUserDetails(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            String email = userDetails.getUsername();
            System.out.println("E-mail do usuário: " + email);
            return new ResponseEntity<>(email,HttpStatus.OK);
        }

        return null;
    }


}

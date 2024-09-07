package com.ismael.movies.infra.security;

import com.ismael.movies.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    TokenService tokenService;

    @Autowired
    UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = this.recoverToken(request);
        if(token != null){
            var login = tokenService.validateToken(token);
            UserDetails user= userRepository.findByLogin(login);

            var authentication = new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request,response);
    }

    private String recoverToken(HttpServletRequest request){
        // Tenta recuperar o token do cabeçalho Authorization
        var authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7); // Remove o prefixo "Bearer "
        }

        // Tenta recuperar o token do cookie
        var cookies = request.getCookies();
        if (cookies != null) {
            var cookie = Arrays.stream(cookies)
                    .filter(c -> "access_token".equals(c.getName()))
                    .findFirst()
                    .orElse(null);
            return cookie != null ? cookie.getValue() : null;
        }

        return null;
    }
}

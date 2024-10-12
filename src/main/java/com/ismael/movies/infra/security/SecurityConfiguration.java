package com.ismael.movies.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Autowired
    SecurityFilter securityFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.GET,"/live").permitAll()
                        .requestMatchers(HttpMethod.GET,"/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/v1/verify").permitAll()
                        .requestMatchers(HttpMethod.GET,"/auth/register-code").permitAll()
                        .requestMatchers(HttpMethod.POST,"/auth/register").permitAll()
                        .requestMatchers(HttpMethod.GET,"/auth/register").permitAll()
                        .requestMatchers(HttpMethod.GET,"/auth/reset").permitAll()
                        .requestMatchers(HttpMethod.POST,"/auth/reset").permitAll()
                        .requestMatchers(HttpMethod.GET, "/auth/user/{token}").permitAll() // Permitir acesso à rota de redefinição de senha
                        .requestMatchers(HttpMethod.POST,"/api/v1/email/send-reset-email").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/v1/email/send-reset-email").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/v1/email/send-verification-code").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/v1/email/send-verification-code").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/v1/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/v1/auth/register").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/v1/movies").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/api/v1/media/hls/upload").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/api/v1/media/img/upload").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/config").permitAll()
                        .requestMatchers(HttpMethod.GET,"/images/logo.png").permitAll()
                        .requestMatchers(HttpMethod.GET,"/images/favicon.ico").permitAll()
                        .requestMatchers(HttpMethod.GET,"/actuator/prometheus").permitAll()
                      // .anyRequest().permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/login")
                                .defaultSuccessUrl("/", true)
                                .permitAll()
                )
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .authenticationEntryPoint((request, response, authException) -> {
                                    response.sendRedirect("/auth/login"); // Redireciona para o login se não autenticado
                                })
                )
                .logout(logout -> logout
                        .logoutUrl("/signout").permitAll()
                        .logoutSuccessUrl("/signout").permitAll()
                    )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}

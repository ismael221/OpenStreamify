package com.ismael.movies.infra.security;

import com.ismael.movies.model.Users.CustomOAuth2User;
import com.ismael.movies.services.CustomOAuth2UserService;
import com.ismael.movies.services.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Autowired
    private CustomOAuth2UserService oauthUserService;
    @Autowired
    SecurityFilter securityFilter;

    @Autowired
    UserService userService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.GET,"/live").permitAll()
                        .requestMatchers(HttpMethod.GET,"/js/*").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/v1/notice/send_alert").permitAll()
                        .requestMatchers(HttpMethod.GET,"/oauth/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/oauth/**").permitAll()
                        .requestMatchers(HttpMethod.GET,"/login/oauth/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/login/oauth/**").permitAll()
                        .requestMatchers(HttpMethod.GET,"/login/oauth2/code").permitAll()
                        .requestMatchers(HttpMethod.POST,"/login/oauth2/code").permitAll()
                        .requestMatchers(HttpMethod.PATCH,"/login/oauth2/code").permitAll()
                        .requestMatchers(HttpMethod.PUT,"/login/oauth2/code").permitAll()
                        .requestMatchers(HttpMethod.GET,"/auth/login").permitAll()
                        .requestMatchers(HttpMethod.GET,"/logout").permitAll()
                        .requestMatchers(HttpMethod.GET,"/signout").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/v1/verify").permitAll()
                        .requestMatchers(HttpMethod.GET,"/auth/register-code").permitAll()
                        .requestMatchers(HttpMethod.POST,"/auth/register").permitAll()
                        .requestMatchers(HttpMethod.GET,"/auth/register").permitAll()
                        .requestMatchers(HttpMethod.GET,"/auth/reset").permitAll()
                        .requestMatchers(HttpMethod.POST,"/auth/reset").permitAll()
                        .requestMatchers(HttpMethod.GET, "/auth/user/{token}").permitAll()
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
                        .requestMatchers(HttpMethod.GET,"/images/*").permitAll()
                        .requestMatchers(HttpMethod.GET,"/actuator/prometheus").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/login")
                                .defaultSuccessUrl("/", true)
                                .permitAll()
                )
                .oauth2Login( oauth2Login ->
                        oauth2Login
                                .loginPage("/login")
                                .userInfoEndpoint( auth -> auth
                                    .userService(oauthUserService)
                                )
                                .successHandler(
                                        (request, response, authentication) -> {

                                            CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();
                                            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
                                            String provider = oauthToken.getAuthorizedClientRegistrationId();
                                            userService.processOAuthPostLogin(oauthUser.getEmail(),oauthUser.getName(),provider);

                                            response.sendRedirect("/");
                                        }
                                )
                                .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/signout").permitAll()
                        .logoutSuccessUrl("/auth/login")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID","access_token")
                )
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
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

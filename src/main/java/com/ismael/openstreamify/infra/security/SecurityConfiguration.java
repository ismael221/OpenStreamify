package com.ismael.openstreamify.infra.security;

import com.ismael.openstreamify.model.Users.CustomOAuth2User;
import com.ismael.openstreamify.services.CustomOAuth2UserService;
import com.ismael.openstreamify.services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final CustomOAuth2UserService oauthUserService;
    final
    SecurityFilter securityFilter;

    final
    UserService userService;

    public SecurityConfiguration(CustomOAuth2UserService oauthUserService, SecurityFilter securityFilter, UserService userService) {
        this.oauthUserService = oauthUserService;
        this.securityFilter = securityFilter;
        this.userService = userService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.GET,"/live").permitAll()
                        .requestMatchers(HttpMethod.GET,"/js/*").permitAll()
                        .requestMatchers(HttpMethod.GET,"/manifest.json").permitAll()
                        .requestMatchers(HttpMethod.GET,"/screenshots/*").permitAll()
                        .requestMatchers(HttpMethod.GET,"/icons/*").permitAll()
                        .requestMatchers(HttpMethod.GET,"/service-worker.js").permitAll()
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

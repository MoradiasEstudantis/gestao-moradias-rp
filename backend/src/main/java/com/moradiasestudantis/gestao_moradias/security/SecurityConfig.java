package com.moradiasestudantis.gestao_moradias.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .cors(withDefaults())
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(HttpMethod.GET, "/", "/error").permitAll()               
                .requestMatchers(HttpMethod.OPTIONS, "/").permitAll()
                
                .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                .requestMatchers("/auth/**").permitAll()
                
                .requestMatchers(HttpMethod.GET, "/residences/{id}").permitAll()
                
                .requestMatchers("/admin/").hasRole("ADMIN")
                .requestMatchers("/user/").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/profile/student", "/profile/student/**").hasRole("ESTUDANTE")
                .requestMatchers(HttpMethod.POST, "/profile/student", "/profile/student/**").hasRole("ESTUDANTE")
                .requestMatchers(HttpMethod.PUT, "/profile/student", "/profile/student/**").hasRole("ESTUDANTE")
                .requestMatchers(HttpMethod.DELETE, "/profile/student", "/profile/student/**").hasRole("ESTUDANTE")
                .requestMatchers("/residences/").hasAnyRole("ADMIN", "USER")
                .requestMatchers("/students/search").hasRole("PROPRIETARIO")

                .anyRequest().authenticated()
            )
            .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        
        configuration.setAllowedHeaders(Arrays.asList("*"));
        
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
   
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
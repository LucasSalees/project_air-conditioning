package com.system_air.project_airconditioning.config;

import java.util.Arrays;

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
	        .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Habilita o CORS configurado abaixo
	        .csrf(csrf -> csrf.disable()) // Desabilita CSRF para API REST
	        .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	        .authorizeHttpRequests(req -> {
	            // Libera o Preflight (OPTIONS) - CRÍTICO para o React
	            req.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll(); 
	            req.requestMatchers("/login/**").permitAll();
	            req.requestMatchers("/auth/**").permitAll();
	            req.anyRequest().authenticated();
	        })
	        .addFilterBefore(securityFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class)
	        .build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
	    CorsConfiguration configuration = new CorsConfiguration();
	    
	    // Adicionamos as origens permitidas (Produção + Localhost)
	    configuration.setAllowedOrigins(Arrays.asList(
	        "https://classarcondicionado.netlify.app", 
	        "http://localhost:5500", 
	        "http://127.0.0.1:5500" // Algumas versões do Live Server usam o IP
	    ));
	    
	    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
	    configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept"));
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
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
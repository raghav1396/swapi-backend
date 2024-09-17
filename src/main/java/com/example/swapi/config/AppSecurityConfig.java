package com.example.swapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig {
  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.applyPermitDefaultValues();
    configuration.setAllowedOrigins(Arrays.asList("*"));
    configuration.setAllowedMethods(Arrays.asList("*"));
    configuration.setAllowedHeaders(Arrays.asList("*"));
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http
      .cors(Customizer.withDefaults())
      .authorizeHttpRequests(request -> request
          .requestMatchers(
            new AntPathRequestMatcher("/swagger-ui/*"),
            new AntPathRequestMatcher("/actuator/**"),
            new AntPathRequestMatcher("/v3/api-docs"),
            new AntPathRequestMatcher("/v3/api-docs/*")
          )
          .permitAll()
          .anyRequest()
          .authenticated()
      ).httpBasic(Customizer.withDefaults())
      .build();
  }

}

package com.yosik.basic.config;

import static org.springframework.security.config.Customizer.withDefaults;

import com.yosik.basic.services.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(authz -> authz.anyRequest().authenticated())
            .httpBasic(withDefaults());
    return http.build();
  }

  @Bean
  public UserDetailsService userDetailsService() {
    return new CustomUserDetailsService();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    // NoOpPasswordEncoder is used for simplicity, use a stronger encoder in production
    return NoOpPasswordEncoder.getInstance();
  }
}
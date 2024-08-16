<<<<<<<< HEAD:examples/multiple_providers/src/main/java/com/yosik/examples/multiple_providers/config/SecurityConfig.java
package com.yosik.examples.multiple_providers.config;

import com.yosik.examples.multiple_providers.services.CustomUserDetailsService;
========
package com.github.mikalaid.examples.basic.config;

import static org.springframework.security.config.Customizer.withDefaults;

import com.github.mikalaid.examples.basic.services.CustomUserDetailsService;
>>>>>>>> develop:examples/basic/src/main/java/com/github/mikalaid/examples/basic/config/SecurityConfig.java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

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
    return NoOpPasswordEncoder.getInstance();
  }
}
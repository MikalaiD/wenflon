package com.yosik.example.services;

import java.util.Arrays;
import java.util.List;

import com.yosik.example.config.user.CustomUserDetails;
import com.yosik.example.config.user.FeaturesRolloutGroup;
import com.yosik.example.config.user.Market;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


public class CustomUserDetailsService implements UserDetailsService {

    private List<CustomUserDetails> users = Arrays.asList(
            new CustomUserDetails("euUser", "password1", Market.EU, FeaturesRolloutGroup.PUBLIC_TESTER, Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"))),
            new CustomUserDetails("usUser", "password2", Market.US, FeaturesRolloutGroup.REGULAR, Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")))
    );

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return users.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
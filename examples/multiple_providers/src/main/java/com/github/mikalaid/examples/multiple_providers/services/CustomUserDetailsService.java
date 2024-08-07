package com.github.mikalaid.examples.multiple_providers.services;


import com.github.mikalaid.examples.multiple_providers.config.user.FeaturesRolloutGroup;
import com.github.mikalaid.examples.multiple_providers.config.user.CustomUserDetails;
import com.github.mikalaid.examples.multiple_providers.config.user.Market;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Arrays;
import java.util.List;


public class CustomUserDetailsService implements UserDetailsService {

    public static final String ROLE_USER = "ROLE_USER";
    private List<CustomUserDetails> users = Arrays.asList(
            new CustomUserDetails("euUser", "password1", Market.EU, FeaturesRolloutGroup.PUBLIC_TESTER, Arrays.asList(new SimpleGrantedAuthority(ROLE_USER))),
            new CustomUserDetails("usUser", "password2", Market.US, FeaturesRolloutGroup.REGULAR, Arrays.asList(new SimpleGrantedAuthority(ROLE_USER))),
            new CustomUserDetails("usUserVIP", "password3", Market.US, FeaturesRolloutGroup.VIP, Arrays.asList(new SimpleGrantedAuthority(ROLE_USER)))
    );

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return users.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
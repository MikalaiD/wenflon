package com.yosik.basic;


import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
public class CustomUserDetails implements UserDetails {
    private final String username;
    private final String password;
    private final Market market;
    private final FeaturesRolloutGroup rolloutGroup;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(final String username, final String password, final Market market, final FeaturesRolloutGroup rolloutGroup, final Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.market = market;
        this.authorities = authorities;
        this.rolloutGroup = rolloutGroup;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

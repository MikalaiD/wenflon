package com.github.mikalaid.examples.multiple_providers.config;

import com.github.mikalaid.examples.multiple_providers.config.user.FeaturesRolloutGroup;
import com.github.mikalaid.examples.multiple_providers.config.user.Market;
import com.github.mikalaid.examples.multiple_providers.config.user.CustomUserDetails;
import com.github.mikalaid.wenflon.PivotProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
public class WenflonConfig {

    @Bean
    PivotProvider<String> defaultPivotProvider(){
        return ()-> Optional.of(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .map(CustomUserDetails.class::cast)
                .map(CustomUserDetails::getMarket)
                .map(Market::name)
                .orElseThrow();
    }
    @Bean
    PivotProvider<String> rolloutGroupPivotProvider(){
        return ()-> Optional.of(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .map(CustomUserDetails.class::cast)
                .map(CustomUserDetails::getRolloutGroup)
                .map(FeaturesRolloutGroup::name)
                .orElseThrow();
    }
}


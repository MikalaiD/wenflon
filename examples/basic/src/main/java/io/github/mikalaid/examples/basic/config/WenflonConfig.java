package io.github.mikalaid.examples.basic.config;

import io.github.mikalaid.examples.basic.config.user.CustomUserDetails;
import io.github.mikalaid.examples.basic.config.user.Market;
import io.github.mikalaid.wenflon.core.PivotProvider;
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
}


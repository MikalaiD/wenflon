package com.yosik.basic;

import com.yosik.wenflon.PivotProvider;
import com.yosik.wenflon.WenflonBeanPostprocessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
public class Config {
    @Bean
    WenflonBeanPostprocessor wenflonBeanPostprocessor() {
        return new WenflonBeanPostprocessor();
    }

    @Bean
    PivotProvider<String> defaultPivotProvider(){
        return ()-> Optional.of(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .map(CustomUserDetails.class::cast)
                .map(CustomUserDetails::getMarket)
                .map(Market::name)
                .orElseThrow();
    }
}


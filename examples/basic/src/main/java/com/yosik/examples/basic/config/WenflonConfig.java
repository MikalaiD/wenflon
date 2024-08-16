<<<<<<<< HEAD:examples/basic/src/main/java/com/yosik/examples/basic/config/WenflonConfig.java
package com.yosik.examples.basic.config;

import com.yosik.examples.basic.config.user.CustomUserDetails;
import com.yosik.examples.basic.config.user.Market;
import com.yosik.wenflon.PivotProvider;
========
package com.github.mikalaid.examples.basic.config;

import com.github.mikalaid.examples.basic.config.user.CustomUserDetails;
import com.github.mikalaid.examples.basic.config.user.Market;
import com.github.mikalaid.wenflon.core.PivotProvider;
>>>>>>>> develop:examples/basic/src/main/java/com/github/mikalaid/examples/basic/config/WenflonConfig.java
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

package io.github.mikalaid.examples.complex_conditions.config;

import io.github.mikalaid.examples.complex_conditions.application.port.out.info.ChipInfoPort;
import io.github.mikalaid.examples.complex_conditions.application.port.out.info.PetClassificatorPort;
import io.github.mikalaid.wenflon.core.PivotProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class WenflonConfig {
    @Bean("petClassificatorProvider")
    PivotProvider<String> getInternalPetClassificator(PetClassificatorPort petClassificatorPort){
        return petClassificatorPort::classify;
    }

    @Bean("chipInfoProvider")
    PivotProvider<BigDecimal> getChipInfoPivotProvider(ChipInfoPort chipInfoPort){
        return chipInfoPort::getAnimalAge;
    }
}

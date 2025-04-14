package io.github.mikalaid.examples.complex_conditions.domain.service.info;

import io.github.mikalaid.examples.complex_conditions.application.port.out.info.PetClassificatorPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

@Service("internalPetClassificator")
@SessionScope
public class InternalPetClassificator implements PetClassificatorPort { //todo add this to docs as an example of internal pivot provider with inheritance

    private final String animalType;

    public InternalPetClassificator(@Value("#{request.getHeader('Animal-Type')}") final String animalType) {
        this.animalType = animalType;
    }

    @Override
    public String classify() { //todo refactor to enum
        return switch (animalType) {
            case "duck" -> "wild";
            case "doc", "cat" -> "domestic_small";
            case "cow", "horse" -> "domestic_large";
            case "chupakabra" -> "exotic";
            default -> "unknown";
        };
    }

}

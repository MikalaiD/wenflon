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
    public String classify() { //todo refactor to enum FINISHED HERE - request falls in here, however, need to come up with visible output e.g. "PetPharmacy confirms order"
        //todo check there are no multiple useless runs for conditions check
        return switch (animalType) {
            case "duck" -> "wild";
            case "doc", "cat" -> "domestic_small";
            case "cow", "horse" -> "domestic_large";
            case "chupakabra" -> "exotic";
            default -> "unknown";
        };
    }

}

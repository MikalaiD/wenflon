package io.github.mikalaid.examples.multiple_providers.services;

import io.github.mikalaid.examples.multiple_providers.ports.DecisionEngine;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class NewInsuranceRiskEngine implements DecisionEngine {

    private final Random random = new Random();
    @Override
    public String rank() {
        return "NEW ENGINE calculated your insurance risk score  as " + random.nextInt(9);
    }
}

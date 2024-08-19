package io.github.mikalaid.examples.basic.services;

import io.github.mikalaid.examples.basic.ports.DecisionEngine;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class StdInsuranceRiskEngine implements DecisionEngine {
    private final Random random = new Random();
    @SneakyThrows
    @Override
    public String rank() {
        Thread.sleep(2000);
        return "Your insurance risk score is " + random.nextInt(10);
    }
}

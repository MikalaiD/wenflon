package com.yosik.basic;

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
        return "We've taken into account 42 parameters and your risk grade is " + random.nextInt(10);
    }
}

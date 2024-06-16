package com.yosik.basic;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class NewInsuranceRiskEngine implements DecisionEngine {

    private final Random random = new Random();
    @Override
    public String rank() {
        return "We've taken into account 123 parameters and your risk grade is " + random.nextInt(9);
    }
}

<<<<<<<< HEAD:examples/basic/src/main/java/com/yosik/examples/basic/services/NewInsuranceRiskEngine.java
package com.yosik.examples.basic.services;

import com.yosik.examples.basic.ports.DecisionEngine;
========
package com.github.mikalaid.examples.basic.services;

import com.github.mikalaid.examples.basic.ports.DecisionEngine;
>>>>>>>> develop:examples/basic/src/main/java/com/github/mikalaid/examples/basic/services/NewInsuranceRiskEngine.java
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class NewInsuranceRiskEngine implements DecisionEngine {

    private final Random random = new Random();
    @Override
    public String rank() {
        return "NEW ENGINE calculated your insurance risk score as " + random.nextInt(9);
    }
}

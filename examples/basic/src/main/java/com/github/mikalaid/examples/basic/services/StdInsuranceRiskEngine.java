<<<<<<<< HEAD:examples/basic/src/main/java/com/yosik/examples/basic/services/StdInsuranceRiskEngine.java
package com.yosik.examples.basic.services;

import com.yosik.examples.basic.ports.DecisionEngine;
========
package com.github.mikalaid.examples.basic.services;

import com.github.mikalaid.examples.basic.ports.DecisionEngine;
>>>>>>>> develop:examples/basic/src/main/java/com/github/mikalaid/examples/basic/services/StdInsuranceRiskEngine.java
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

<<<<<<<< HEAD:examples/multiple_providers/src/main/java/com/yosik/examples/multiple_providers/services/NewInsuranceRiskEngine.java
package com.yosik.examples.multiple_providers.services;

import com.yosik.examples.multiple_providers.ports.DecisionEngine;
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
<<<<<<<< HEAD:examples/multiple_providers/src/main/java/com/yosik/examples/multiple_providers/services/NewInsuranceRiskEngine.java
        return "NEW ENGINE calculated your insurance risk score  as " + random.nextInt(9);
========
        return "NEW ENGINE calculated your insurance risk score as " + random.nextInt(9);
>>>>>>>> develop:examples/basic/src/main/java/com/github/mikalaid/examples/basic/services/NewInsuranceRiskEngine.java
    }
}

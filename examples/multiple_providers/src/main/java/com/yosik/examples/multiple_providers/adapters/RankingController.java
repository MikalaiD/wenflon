<<<<<<<< HEAD:examples/multiple_providers/src/main/java/com/yosik/examples/multiple_providers/adapters/RankingController.java
package com.yosik.examples.multiple_providers.adapters;

import com.yosik.examples.multiple_providers.ports.DecisionEngine;
========
package com.github.mikalaid.examples.basic.adapters;

import com.github.mikalaid.examples.basic.ports.DecisionEngine;
>>>>>>>> develop:examples/basic/src/main/java/com/github/mikalaid/examples/basic/adapters/RankingController.java
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class RankingController {

    private final DecisionEngine decisionEngine;

    @GetMapping(path = "/rank")
    public ResponseEntity<String> rank(){
        return ResponseEntity.ok(decisionEngine.rank());
    }
}
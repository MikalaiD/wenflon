<<<<<<<< HEAD:examples/basic/src/main/java/com/yosik/examples/basic/adapters/RankingController.java
package com.yosik.examples.basic.adapters;

import com.yosik.examples.basic.ports.DecisionEngine;
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

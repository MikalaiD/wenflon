package com.yosik.examples.multiple_providers.adapters;

import com.yosik.examples.multiple_providers.ports.DecisionEngine;
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

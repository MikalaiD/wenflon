package com.yosik.basic;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BarmanController {

    private final Barman barman;

    public BarmanController(Barman barman) {
        this.barman = barman;
    }

    @GetMapping(path = "/enter-the-bar")
    public ResponseEntity<String> enterTheBar(){
        return ResponseEntity.ok(barman.pourTheDrinks());
    }
}

package com.github.mikalaid.examples.multiple_providers.adapters;

import com.github.mikalaid.examples.multiple_providers.ports.KYCScanner;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class KYCController {

    private final KYCScanner kycScanner;

    @GetMapping(path = "/scan")
    public ResponseEntity<String> scan(){
        return ResponseEntity.ok(kycScanner.checkCustomer());
    }
}

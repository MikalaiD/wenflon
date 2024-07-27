package com.yosik.examples.multiple_providers.services;

import com.yosik.examples.multiple_providers.ports.KYCScanner;
import org.springframework.stereotype.Service;

@Service
public class ShallowKYCScanner implements KYCScanner {
    @Override
    public String checkCustomer() {
        return "Looks legit to me... Approved";
    }
}

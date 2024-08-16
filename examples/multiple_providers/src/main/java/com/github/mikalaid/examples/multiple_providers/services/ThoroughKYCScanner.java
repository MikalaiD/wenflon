package com.github.mikalaid.examples.multiple_providers.services;

import com.github.mikalaid.examples.multiple_providers.ports.KYCScanner;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ThoroughKYCScanner  implements KYCScanner {
    @Override
    @SneakyThrows
    public String checkCustomer() {
        log.info("Verifying client's genealogy tree...");
        Thread.sleep(5000);
        return "We can trust this gentleman";
    }
}

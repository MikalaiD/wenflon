package io.github.mikalaid.examples.multiple_providers.ports;

import io.github.mikalaid.wenflon.core.Wenflon;

@Wenflon(pivotProviderBeanName = "rolloutGroupPivotProvider")
public interface KYCScanner {
    // Know Your Customer (KYC) - guidelines and regulations in financial services require professionals to verify
    // the identity, suitability, and risks involved with maintaining a business relationship with a customer (Wiki)
    String checkCustomer();
}

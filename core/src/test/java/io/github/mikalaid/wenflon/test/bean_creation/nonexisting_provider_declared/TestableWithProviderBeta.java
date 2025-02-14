package io.github.mikalaid.wenflon.test.bean_creation.nonexisting_provider_declared;

import io.github.mikalaid.wenflon.core.Wenflon;

@Wenflon(pivotProviderBeanNames = "providerY")
public interface TestableWithProviderBeta {
    default String test(){
        return this.getClass().getCanonicalName();
    }
}

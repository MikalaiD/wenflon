package io.github.mikalaid.wenflon.test.bean_creation.nonexisting_provider_declared;

import io.github.mikalaid.wenflon.core.Wenflon;

@Wenflon(pivotProviderBeanNames = "providerX")
public interface TestableWithProviderAlpha {
    default String test(){
        return this.getClass().getCanonicalName();
    }
}

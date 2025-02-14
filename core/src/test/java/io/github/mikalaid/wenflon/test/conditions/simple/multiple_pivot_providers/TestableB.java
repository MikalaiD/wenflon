package io.github.mikalaid.wenflon.test.conditions.simple.multiple_pivot_providers;

import io.github.mikalaid.wenflon.core.Wenflon;

@Wenflon(pivotProviderBeanNames = "providerBeta")
public interface TestableB {
    default String test(){
        return this.getClass().getCanonicalName();
    }
}

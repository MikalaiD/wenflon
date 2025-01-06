package io.github.mikalaid.wenflon.test._common;

import io.github.mikalaid.wenflon.core.Wenflon;

@Wenflon(pivotProviderBeanNames = "providerZ")
public interface TestableWithProviderZ {
    default String test(){
        return this.getClass().getCanonicalName();
    }
}

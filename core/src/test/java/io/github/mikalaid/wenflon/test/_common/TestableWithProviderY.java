package io.github.mikalaid.wenflon.test._common;

import io.github.mikalaid.wenflon.core.Wenflon;

@Wenflon(pivotProviderBeanName = "providerY")
public interface TestableWithProviderY {
    default String test(){
        return this.getClass().getCanonicalName();
    }
}

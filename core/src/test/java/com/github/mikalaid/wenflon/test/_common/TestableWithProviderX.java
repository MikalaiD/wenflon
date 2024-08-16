package com.github.mikalaid.wenflon.test._common;

import com.github.mikalaid.wenflon.core.Wenflon;

@Wenflon(pivotProviderBeanName = "providerX")
public interface TestableWithProviderX {
    default String test(){
        return this.getClass().getCanonicalName();
    }
}

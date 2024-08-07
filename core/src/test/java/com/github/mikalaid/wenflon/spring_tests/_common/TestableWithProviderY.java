package com.github.mikalaid.wenflon.spring_tests._common;

import com.github.mikalaid.wenflon.Wenflon;

@Wenflon(pivotProviderBeanName = "providerY")
public interface TestableWithProviderY {
    default String test(){
        return this.getClass().getCanonicalName();
    }
}

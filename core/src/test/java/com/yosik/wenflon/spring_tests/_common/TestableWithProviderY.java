package com.yosik.wenflon.spring_tests._common;

import com.yosik.wenflon.Wenflon;

@Wenflon(pivotProviderBeanName = "providerY")
public interface TestableWithProviderY {
    default String test(){
        return this.getClass().getCanonicalName();
    }
}

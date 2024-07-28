package com.yosik.wenflon.spring_tests._common;

import com.yosik.wenflon.Wenflon;

@Wenflon(pivotProviderBeanName = "providerX")
public interface TestableWithProviderX {
    default String test(){
        return this.getClass().getCanonicalName();
    }
}

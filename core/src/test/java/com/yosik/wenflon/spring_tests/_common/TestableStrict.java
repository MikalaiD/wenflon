package com.yosik.wenflon.spring_tests._common;

import com.yosik.wenflon.Wenflon;

@Wenflon(soleConditionalImplAsImplicitDefault = false)
public interface TestableStrict {
    default String test(){
        return this.getClass().getCanonicalName();
    }
}

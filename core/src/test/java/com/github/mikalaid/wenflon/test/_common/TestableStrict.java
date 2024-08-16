package com.github.mikalaid.wenflon.test._common;

import com.github.mikalaid.wenflon.core.Wenflon;

@Wenflon(soleConditionalImplAsImplicitDefault = false)
public interface TestableStrict {
    default String test(){
        return this.getClass().getCanonicalName();
    }
}

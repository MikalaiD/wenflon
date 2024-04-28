package com.yosik.wenflon.spring_tests.common;

import com.yosik.wenflon.Wenflon;

@Wenflon
public interface Testable {
    default String test(){
        return this.getClass().getCanonicalName();
    }
}

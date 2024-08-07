package com.github.mikalaid.wenflon.spring_tests._common;

import com.github.mikalaid.wenflon.Wenflon;

@Wenflon
public interface Testable {
    default String test(){
        return this.getClass().getCanonicalName();
    }
}

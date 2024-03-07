package com.yosik.wenflon.test_classes;

import com.yosik.wenflon.Wenflon;

@Wenflon
public interface Testable {
    default String test(){
        return this.getClass().getCanonicalName();
    };
}

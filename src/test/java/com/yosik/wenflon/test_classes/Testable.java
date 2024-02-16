package com.yosik.wenflon.test_classes;

public interface Testable {
    default String test(){
        return this.getClass().getCanonicalName();
    };
}

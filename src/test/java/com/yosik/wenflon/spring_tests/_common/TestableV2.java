package com.yosik.wenflon.spring_tests._common;

import com.yosik.wenflon.Wenflon;

@Wenflon
public interface TestableV2 {
    default String testV2(){
        return this.getClass().getCanonicalName();
    }
}

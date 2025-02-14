package io.github.mikalaid.wenflon.test.bean_creation.two_implementations;

import io.github.mikalaid.wenflon.core.Wenflon;

@Wenflon
public interface Testable {
    default String test(){
        return this.getClass().getCanonicalName();
    }
}

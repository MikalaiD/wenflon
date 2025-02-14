package io.github.mikalaid.wenflon.test.bean_creation.one_implementation;

import io.github.mikalaid.wenflon.core.Wenflon;

@Wenflon
public interface Testable {
    default String test(){
        return this.getClass().getCanonicalName();
    }
}

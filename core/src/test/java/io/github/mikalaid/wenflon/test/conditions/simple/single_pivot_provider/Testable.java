package io.github.mikalaid.wenflon.test.conditions.simple.single_pivot_provider;

import io.github.mikalaid.wenflon.core.Wenflon;

@Wenflon
public interface Testable {
    default String test(){
        return this.getClass().getCanonicalName();
    }
}

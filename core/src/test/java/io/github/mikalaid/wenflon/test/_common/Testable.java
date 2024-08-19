package io.github.mikalaid.wenflon.test._common;

import io.github.mikalaid.wenflon.core.Wenflon;

@Wenflon
public interface Testable {
    default String test(){
        return this.getClass().getCanonicalName();
    }
}

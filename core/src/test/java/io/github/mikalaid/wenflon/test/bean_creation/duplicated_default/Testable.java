package io.github.mikalaid.wenflon.test.bean_creation.duplicated_default;

import io.github.mikalaid.wenflon.core.Wenflon;

@Wenflon
public interface Testable {
    default String test(){
        return this.getClass().getCanonicalName();
    }
}

package io.github.mikalaid.wenflon.test.bean_creation.multiple_complex_condition_type_for_same_wenflon;

import io.github.mikalaid.wenflon.core.Wenflon;

@Wenflon(pivotProviderBeanNames = {"providerAlpha", "providerBeta"} ) //todo think if beans names should be specified here.. maybe config is enough to steer it
public interface Testable {
    default String test(){
        return this.getClass().getCanonicalName();
    }
}

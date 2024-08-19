package io.github.mikalaid.examples.multiple_providers.ports;

import io.github.mikalaid.wenflon.core.Wenflon;

@Wenflon(pivotProviderBeanName = "defaultPivotProvider")
public interface DecisionEngine {
    String rank();
}

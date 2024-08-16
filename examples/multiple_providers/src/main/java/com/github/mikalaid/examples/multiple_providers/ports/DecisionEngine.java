package com.github.mikalaid.examples.multiple_providers.ports;

import com.github.mikalaid.wenflon.Wenflon;

@Wenflon(pivotProviderBeanName = "defaultPivotProvider")
public interface DecisionEngine {
    String rank();
}
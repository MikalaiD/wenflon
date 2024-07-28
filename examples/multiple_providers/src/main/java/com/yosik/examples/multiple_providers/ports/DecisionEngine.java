package com.yosik.examples.multiple_providers.ports;

import com.yosik.wenflon.Wenflon;

@Wenflon(pivotProviderBeanName = "defaultPivotProvider")
public interface DecisionEngine {
    String rank();
}

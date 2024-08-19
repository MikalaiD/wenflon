package io.github.mikalaid.examples.basic.ports;

import io.github.mikalaid.wenflon.core.Wenflon;

@Wenflon
public interface DecisionEngine {
    String rank();
}

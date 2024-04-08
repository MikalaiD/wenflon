package com.yosik.wenflon;

public interface PivotProvider<T> {
    T getPivot(String pivotName);
}

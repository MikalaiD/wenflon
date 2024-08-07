package com.github.mikalaid.wenflon;

@FunctionalInterface
public interface PivotProvider<T>{
  T getPivot();
}

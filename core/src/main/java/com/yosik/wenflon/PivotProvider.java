package com.yosik.wenflon;

@FunctionalInterface
public interface PivotProvider<T>{
  T getPivot();
}

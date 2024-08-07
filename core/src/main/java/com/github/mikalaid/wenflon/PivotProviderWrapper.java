package com.github.mikalaid.wenflon;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class PivotProviderWrapper<T> implements PivotProvider<T> {

  @Getter
  private final String beanName;

  private final PivotProvider<T> pivotProvider;

  @Override
  public T getPivot() {
    return pivotProvider.getPivot();
  }
}

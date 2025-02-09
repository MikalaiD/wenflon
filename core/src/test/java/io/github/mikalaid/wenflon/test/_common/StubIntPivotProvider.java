package io.github.mikalaid.wenflon.test._common;

import io.github.mikalaid.wenflon.core.PivotProvider;

public class StubIntPivotProvider implements PivotProvider<Integer> {

  @Override
  public Integer getPivot() {
    return null;
  }
}

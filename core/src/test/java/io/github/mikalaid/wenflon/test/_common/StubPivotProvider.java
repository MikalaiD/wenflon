package io.github.mikalaid.wenflon.test._common;

import io.github.mikalaid.wenflon.core.PivotProvider;

public class StubPivotProvider implements PivotProvider<String> {

  @Override
  public String getPivot() {
    return null;
  }
}

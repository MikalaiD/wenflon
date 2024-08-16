package com.github.mikalaid.wenflon.test._common;

import com.github.mikalaid.wenflon.core.PivotProvider;

public class StubPivotProvider implements PivotProvider<String> {

  @Override
  public String getPivot() {
    return null;
  }
}

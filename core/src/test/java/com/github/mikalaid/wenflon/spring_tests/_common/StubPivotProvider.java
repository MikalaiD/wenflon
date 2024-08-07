package com.github.mikalaid.wenflon.spring_tests._common;

import com.github.mikalaid.wenflon.PivotProvider;

public class StubPivotProvider implements PivotProvider<String> {

  @Override
  public String getPivot() {
    return null;
  }
}

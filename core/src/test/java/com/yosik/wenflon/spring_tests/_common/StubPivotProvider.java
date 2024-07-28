package com.yosik.wenflon.spring_tests._common;

import com.yosik.wenflon.PivotProvider;

public class StubPivotProvider implements PivotProvider<String> {

  @Override
  public String getPivot() {
    return null;
  }
}

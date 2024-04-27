package com.yosik.wenflon;

import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FinalAssembler {

  private final List<WenflonDynamicProxy<?>> wenflons;
  private final WenflonProperties properties;

  @PostConstruct
  private void assemble() {
    System.out.println("Assembling");
  }
}

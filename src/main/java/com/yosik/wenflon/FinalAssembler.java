package com.yosik.wenflon;

import jakarta.annotation.PostConstruct;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.support.BeanDefinitionValidationException;

@RequiredArgsConstructor
public class FinalAssembler { // todo make not public?

  private final List<ProxyFactory<?>> wenflons;
  private final DynamicProxyProperties properties;
  private final List<PivotProvider<?>> pivotProviders;

  @PostConstruct
  private void assemble() {
    validate();
    System.out.println("Assembling");
    wenflons.forEach(wenflon -> wenflon.addConditions(properties));
    wenflons.forEach(wenflon -> wenflon.addPivotProvider(pivotProviders));
  }

  private void validate() {
    verifyAtLeastOnePivotProviderIsPresent();
  }

  private void verifyAtLeastOnePivotProviderIsPresent() {
    if (pivotProviders.isEmpty()) {
      throw new BeanDefinitionValidationException(
          "At least one bean implementing PivotProvider should be present in context");
    }
  }
}

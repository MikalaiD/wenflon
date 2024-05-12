package com.yosik.wenflon;

import jakarta.annotation.PostConstruct;

import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionValidationException;

@RequiredArgsConstructor
@Slf4j
public class FinalAssembler { // todo make not public?

  private final List<WenflonDynamicProxy<?>> wenflons;
  private final WenflonProperties properties;
  private final List<PivotProvider<?>> pivotProviders;

  @PostConstruct
  private void assemble() {
    validate();
    System.out.println("Assembling"); //todo add appropriate logging
    wenflons.forEach(wenflon -> wenflon.addConditions(properties));
    wenflons.forEach(wenflon -> wenflon.addPivotProvider(pivotProviders));
    //todo add post validation - throw an exception in case there is something wrong with conditions - e.g. if for a given wenflon
    // there won't be any case when any implementation is chosen

    // todo add properties to resolve the following situations:
    // one implementation, condition present, condition is not met - strict(throw exception), soft(one implementation is default one, just always used)
    // 2+ implementations, condition present, condition is not met - strict(throw exception), soft(fall back to some default implementation)
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

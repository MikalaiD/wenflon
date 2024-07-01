package com.yosik.wenflon;

import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionValidationException;

@RequiredArgsConstructor
@Slf4j
class FinalAssembler {

  private final List<WenflonDynamicProxy<?>> wenflons;
  private final WenflonProperties properties;
  private final List<PivotProvider<?>> pivotProviders;

  @PostConstruct
  private void assemble() {
    log.info("Starting assembling - adding conditions and pivot providers");
    validateAtLeastOnePivotProviderIsPresent();
    wenflons.forEach(wenflon -> wenflon.addConditions(properties));
    wenflons.forEach(WenflonDynamicProxy::trySetImplicitDefault);
    wenflons.forEach(wenflon -> wenflon.addPivotProvider(pivotProviders));
  }

  private void validateAtLeastOnePivotProviderIsPresent() {
    if (pivotProviders.isEmpty()) {
      throw new BeanDefinitionValidationException(
              "At least one bean implementing PivotProvider should be present in context");
    }
  }
}

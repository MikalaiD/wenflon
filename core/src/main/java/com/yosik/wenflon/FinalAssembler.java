package com.yosik.wenflon;

import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionValidationException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;

@RequiredArgsConstructor
@Slf4j
class FinalAssembler {

  private final List<WenflonDynamicProxy<?>> wenflons;
  private final WenflonProperties properties;
  private final List<PivotProvider<?>> pivotProviders;
  private int count = 1;

  @EventListener
  public void handleContextRefresh(final ContextRefreshedEvent event) {
    if(count<1){
      return;
    }
    log.info("All beans have been initialized. Starting assembling - adding conditions and pivot providers");
    count--;
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

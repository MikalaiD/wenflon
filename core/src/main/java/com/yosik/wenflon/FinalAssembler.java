package com.yosik.wenflon;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

@RequiredArgsConstructor
@Slf4j
class FinalAssembler {

  private final List<WenflonDynamicProxy<?>> wenflons;
  private final WenflonProperties properties;
  private final List<PivotProviderWrapper<?>> pivotProviders;
  private boolean allBeansInitialized = false;

  @EventListener
  public void handleContextRefresh(final ContextRefreshedEvent event) {
    if(allBeansInitialized){
      return;
    }
    allBeansInitialized=true;
    log.info("All beans have been initialized. Starting assembling - adding conditions and pivot providers");
    wenflons.forEach(wenflon -> wenflon.addConditions(properties));
    wenflons.forEach(WenflonDynamicProxy::trySetImplicitDefault);
    wenflons.forEach(wenflon -> wenflon.addPivotProvider(pivotProviders));
  }
}

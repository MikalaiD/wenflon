package com.yosik.wenflon;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FinalAssemblerConfig {
  @Bean
  FinalAssembler finalAssembler(
      final List<WenflonDynamicProxy<?>> wenflonDynamicProxies,
      final WenflonProperties properties,
      final List<PivotProvider<?>> pivotProviders) {
    return new FinalAssembler(wenflonDynamicProxies, properties, pivotProviders);
  }
}

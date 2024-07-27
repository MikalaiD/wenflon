package com.yosik.wenflon;

import java.util.List;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@AutoConfiguration
public class Config {
  @Bean
  @Primary
  FinalAssembler finalAssembler(
      final List<WenflonDynamicProxy<?>> wenflonDynamicProxies,
      final WenflonProperties properties,
      final List<PivotProviderWrapper<?>> pivotProviders) {
    return new FinalAssembler(wenflonDynamicProxies, properties, pivotProviders);
  }

  @Bean
  @Primary
  WenflonBeanPostProcessor wenflonBeanPostProcessor(){
    return new WenflonBeanPostProcessor();
  }
}

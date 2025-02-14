package io.github.mikalaid.wenflon.test.bean_creation.nonexisting_provider_declared;

import io.github.mikalaid.wenflon.core.PivotProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("io.github.mikalaid.wenflon.core")
public class TestConfig {

  @Bean
  TestableWithProviderAlpha testableA() {
    return new ServiceA();
  }

  @Bean
  TestableWithProviderBeta testableB() {
    return new ServiceB();
  }

  @Bean
  PivotProvider<String> pivotProvider() {
    return () -> "panda";
  }
}

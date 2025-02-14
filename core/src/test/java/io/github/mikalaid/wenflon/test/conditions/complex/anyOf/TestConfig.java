package io.github.mikalaid.wenflon.test.conditions.complex.anyOf;

import io.github.mikalaid.wenflon.core.PivotProvider;
import io.github.mikalaid.wenflon.test._common.StubIntPivotProvider;
import io.github.mikalaid.wenflon.test._common.StubStringPivotProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("io.github.mikalaid.wenflon.core")
public class TestConfig {

  @Bean
  Testable testableA() {
    return new ServiceA();
  }
  @Bean
  Testable testableB() {
    return new ServiceB();
  }

  @Bean
  PivotProvider<String> providerAlpha() {
    return new StubStringPivotProvider();
  }

  @Bean
  PivotProvider<Integer> providerBeta() {
    return new StubIntPivotProvider();
  }
}

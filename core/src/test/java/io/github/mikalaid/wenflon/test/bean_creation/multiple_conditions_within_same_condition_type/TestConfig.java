package io.github.mikalaid.wenflon.test.bean_creation.multiple_conditions_within_same_condition_type;

import io.github.mikalaid.wenflon.core.PivotProvider;
import io.github.mikalaid.wenflon.test._common.StubIntPivotProvider;
import io.github.mikalaid.wenflon.test._common.StubStringPivotProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("io.github.mikalaid.wenflon.core")
class TestConfig {

  @Bean
  Testable testableA() {
    return new ServiceA();
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

package io.github.mikalaid.wenflon.test.conditions.simple.multiple_pivot_providers;

import io.github.mikalaid.wenflon.core.PivotProvider;
import io.github.mikalaid.wenflon.test._common.StubStringPivotProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("io.github.mikalaid.wenflon.core")
class TestConfig {

  @Bean
  TestableA testableA1() {
    return new ServiceA1();
  }

  @Bean
  TestableA testableA2() {
    return new ServiceA2();
  }

  @Bean
  TestableB testableB1() {
    return new ServiceB1();
  }
  @Bean
  TestableB testableB2() {return new ServiceB2();}

  @Bean
  PivotProvider<String> providerAlpha() {
    return new StubStringPivotProvider();
  }

  @Bean
  PivotProvider<String> providerBeta() {
    return new StubStringPivotProvider();
  }

}

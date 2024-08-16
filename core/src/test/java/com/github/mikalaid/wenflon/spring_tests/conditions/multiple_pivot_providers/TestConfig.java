package com.github.mikalaid.wenflon.spring_tests.conditions.multiple_pivot_providers;

import com.github.mikalaid.wenflon.spring_tests._common.ServiceH;
import com.github.mikalaid.wenflon.Config;
import com.github.mikalaid.wenflon.PivotProvider;
import com.github.mikalaid.wenflon.spring_tests._common.ServiceE;
import com.github.mikalaid.wenflon.spring_tests._common.ServiceF;
import com.github.mikalaid.wenflon.spring_tests._common.ServiceG;
import com.github.mikalaid.wenflon.spring_tests._common.StubPivotProvider;
import com.github.mikalaid.wenflon.spring_tests._common.TestableWithProviderX;
import com.github.mikalaid.wenflon.spring_tests._common.TestableWithProviderY;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(Config.class)
public class TestConfig {

  @Bean
  TestableWithProviderX testableE() {
    return new ServiceE();
  }

  @Bean
  TestableWithProviderX testableF() {
    return new ServiceF();
  }

  @Bean
  TestableWithProviderY testableG() {
    return new ServiceG();
  }
  @Bean
  TestableWithProviderY testableH() {
    return new ServiceH();
  }

  @Bean
  PivotProvider<String> providerX() {
    return new StubPivotProvider();
  }

  @Bean
  PivotProvider<String> providerY() {
    return new StubPivotProvider();
  }

}

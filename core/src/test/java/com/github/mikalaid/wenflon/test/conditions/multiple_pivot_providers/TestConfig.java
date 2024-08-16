package com.github.mikalaid.wenflon.test.conditions.multiple_pivot_providers;

import com.github.mikalaid.wenflon.core.PivotProvider;
import com.github.mikalaid.wenflon.test._common.ServiceE;
import com.github.mikalaid.wenflon.test._common.ServiceF;
import com.github.mikalaid.wenflon.test._common.ServiceG;
import com.github.mikalaid.wenflon.test._common.ServiceH;
import com.github.mikalaid.wenflon.test._common.StubPivotProvider;
import com.github.mikalaid.wenflon.test._common.TestableWithProviderX;
import com.github.mikalaid.wenflon.test._common.TestableWithProviderY;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.github.mikalaid.wenflon.core")
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

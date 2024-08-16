package com.github.mikalaid.wenflon.test.bean_creation.nonexisting_provider_declared;

import com.github.mikalaid.wenflon.core.PivotProvider;
import com.github.mikalaid.wenflon.test._common.ServiceF;
import com.github.mikalaid.wenflon.test._common.ServiceG;
import com.github.mikalaid.wenflon.test._common.TestableWithProviderX;
import com.github.mikalaid.wenflon.test._common.TestableWithProviderY;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.github.mikalaid.wenflon.core")
public class TestConfig {

  @Bean
  TestableWithProviderY testableG() {
    return new ServiceG();
  }

  @Bean
  TestableWithProviderX testableF() {
    return new ServiceF();
  }

  @Bean
  PivotProvider<String> pivotProvider() {
    return () -> "panda";
  }
}

package com.github.mikalaid.wenflon.spring_tests.bean_creation.nonexisting_provider_declared;

import com.github.mikalaid.wenflon.Config;
import com.github.mikalaid.wenflon.PivotProvider;
import com.github.mikalaid.wenflon.spring_tests._common.ServiceF;
import com.github.mikalaid.wenflon.spring_tests._common.ServiceG;
import com.github.mikalaid.wenflon.spring_tests._common.TestableWithProviderX;
import com.github.mikalaid.wenflon.spring_tests._common.TestableWithProviderY;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(Config.class)
@ComponentScan("com.github.mikalaid.wenflon")
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

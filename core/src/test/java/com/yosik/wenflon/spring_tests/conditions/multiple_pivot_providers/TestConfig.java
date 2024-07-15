package com.yosik.wenflon.spring_tests.conditions.multiple_pivot_providers;

import com.yosik.wenflon.Config;
import com.yosik.wenflon.PivotProvider;
import com.yosik.wenflon.spring_tests._common.ServiceE;
import com.yosik.wenflon.spring_tests._common.ServiceF;
import com.yosik.wenflon.spring_tests._common.ServiceG;
import com.yosik.wenflon.spring_tests._common.StubPivotProvider;
import com.yosik.wenflon.spring_tests._common.TestableWithProviderX;
import com.yosik.wenflon.spring_tests._common.TestableWithProviderY;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(Config.class)
public class TestConfig {

  //    @Bean
  //    Testable testableA() {
  //        return new ServiceA();
  //    }
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
  PivotProvider<String> providerX() {
    return new StubPivotProvider();
  }

  @Bean
  PivotProvider<String> providerY() {
    return new StubPivotProvider();
  }

}

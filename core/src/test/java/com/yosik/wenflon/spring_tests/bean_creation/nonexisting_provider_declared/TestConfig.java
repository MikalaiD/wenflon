package com.yosik.wenflon.spring_tests.bean_creation.nonexisting_provider_declared;

import com.yosik.wenflon.Config;
import com.yosik.wenflon.PivotProvider;
import com.yosik.wenflon.WenflonProperties;
import com.yosik.wenflon.spring_tests._common.ServiceB;
import com.yosik.wenflon.spring_tests._common.ServiceC;
import com.yosik.wenflon.spring_tests._common.ServiceF;
import com.yosik.wenflon.spring_tests._common.ServiceG;
import com.yosik.wenflon.spring_tests._common.Testable;
import com.yosik.wenflon.spring_tests._common.TestableWithProviderX;
import com.yosik.wenflon.spring_tests._common.TestableWithProviderY;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(Config.class)
@EnableConfigurationProperties(WenflonProperties.class)
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

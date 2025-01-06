package io.github.mikalaid.wenflon.test.conditions.complex_conditions;

import io.github.mikalaid.wenflon.core.PivotProvider;
import io.github.mikalaid.wenflon.test._common.ServiceE;
import io.github.mikalaid.wenflon.test._common.ServiceF;
import io.github.mikalaid.wenflon.test._common.ServiceG;
import io.github.mikalaid.wenflon.test._common.ServiceH;
import io.github.mikalaid.wenflon.test._common.StubPivotProvider;
import io.github.mikalaid.wenflon.test._common.TestableWithProviderX;
import io.github.mikalaid.wenflon.test._common.TestableWithProviderY;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("io.github.mikalaid.wenflon.core")
public class TestConfig {

  @Bean
  TestableWithProviderX testableI() {
    return new ServiceE();
  }
}

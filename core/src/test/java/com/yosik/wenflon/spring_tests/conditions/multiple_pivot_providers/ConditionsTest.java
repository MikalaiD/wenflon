package com.yosik.wenflon.spring_tests.conditions.multiple_pivot_providers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.yosik.wenflon.Config;
import com.yosik.wenflon.PivotProvider;
import com.yosik.wenflon.WenflonProperties;
import com.yosik.wenflon.spring_tests._common.ServiceA;
import com.yosik.wenflon.spring_tests._common.ServiceB;
import com.yosik.wenflon.spring_tests._common.ServiceC;
import com.yosik.wenflon.spring_tests._common.Testable;
import java.util.stream.Stream;

import com.yosik.wenflon.spring_tests._common.TestableWithProviderX;
import com.yosik.wenflon.spring_tests._common.TestableWithProviderY;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties(WenflonProperties.class)
@ContextConfiguration(classes = {TestConfig.class, Config.class})
@TestPropertySource("classpath:conditions/application-test_multiple_providers.properties")
class ConditionsTest {

  @Autowired
  TestableWithProviderX wenflonProxyBeanX;
  @Autowired
  TestableWithProviderY wenflonProxyBeanY;

  @MockBean(name = "providerX") PivotProvider<String> pivotProviderX;
  @MockBean(name = "providerY") PivotProvider<String> pivotProviderY;

  @Test
  void wenflons_use_appropriate_providers(){
    wenflonProxyBeanX.test();
    wenflonProxyBeanY.test();

    verify(pivotProviderX, times(1)).getPivot();
    verify(pivotProviderY, times(1)).getPivot();
  }
}

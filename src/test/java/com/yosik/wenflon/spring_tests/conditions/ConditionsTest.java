package com.yosik.wenflon.spring_tests.conditions;

import com.yosik.wenflon.PivotProvider;
import com.yosik.wenflon.DynamicProxyProperties;
import com.yosik.wenflon.spring_tests._common.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.stream.Stream;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties(DynamicProxyProperties.class)
@ContextConfiguration(classes = TestConfig.class)
@TestPropertySource("classpath:conditions/application-test.properties")
public class ConditionsTest {

  @Autowired Testable wenflonProxyBean;
  @Autowired TestableV2 wenflonProxyBeanV2;

  @Autowired
  @Qualifier("testableA")
  Testable testableA;

  @Autowired
  @Qualifier("testableB")
  Testable testableB;

  @Autowired
  @Qualifier("testableB")
  TestableV2 testableBV2;

  @Autowired
  @Qualifier("testableC")
  TestableV2 testableC;

  @MockBean PivotProvider<String> pivotProvider;

  @ParameterizedTest
  @MethodSource("getTestConfigurations")
  void condition_work(Class<?> implementationClass, Class<?> implementationClassV2, String pivot) {
    // given
    when(pivotProvider.getPivot()).thenReturn(pivot);

    // when
    String result = wenflonProxyBean.test();

    // then
    Assertions.assertThat(result).isEqualTo(implementationClass.getCanonicalName());
  }

  @ParameterizedTest
  @MethodSource("getParallelTestConfigurations")
  void condition_work_with_2_proxies_using_same_bean(
      Class<?> implementationClass, Class<?> implementationClassV2, String pivot) {
    // given
    when(pivotProvider.getPivot()).thenReturn(pivot);

    // when
    String result = wenflonProxyBean.test();
    String resultV2 = wenflonProxyBeanV2.testV2();

    // then
    Assertions.assertThat(result).isEqualTo(implementationClass.getCanonicalName());
    Assertions.assertThat(resultV2).isEqualTo(implementationClassV2.getCanonicalName());
  }

  public static Stream<Arguments> getTestConfigurations() {
    return Stream.of(
        Arguments.of(ServiceA.class, ServiceC.class, "panda"),
        Arguments.of(ServiceB.class, ServiceB.class, "grizzly"),
        Arguments.of(ServiceA.class, ServiceB.class, "koala"),
        Arguments.of(ServiceB.class, ServiceB.class, "white bear"));
  }

  public static Stream<Arguments> getParallelTestConfigurations() {
    return Stream.of(
        Arguments.of(ServiceA.class, ServiceC.class, "panda"),
        Arguments.of(ServiceB.class, ServiceB.class, "grizzly"));
  }
  //todo add exception when there is bean but there is no condition.
}

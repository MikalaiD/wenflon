package com.yosik.wenflon.spring_tests.conditions.happy_path;

import com.yosik.wenflon.FinalAssemblerConfig;
import com.yosik.wenflon.PivotProvider;
import com.yosik.wenflon.WenflonProperties;
import com.yosik.wenflon.spring_tests._common.ServiceA;
import com.yosik.wenflon.spring_tests._common.ServiceB;
import com.yosik.wenflon.spring_tests._common.ServiceC;
import com.yosik.wenflon.spring_tests._common.Testable;
import org.assertj.core.api.Assertions;
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

import java.util.stream.Stream;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties(WenflonProperties.class)
@ContextConfiguration(classes = {TestConfig.class, FinalAssemblerConfig.class})
@TestPropertySource("classpath:conditions/application-test_happypath.properties")
public class ConditionsTest {

  @Autowired Testable wenflonProxyBean;

  @MockBean PivotProvider<String> pivotProvider;

  @ParameterizedTest
  @MethodSource("getTestConfigurations")
  void condition_work(Class<?> implementationClass, String pivot) {
    // given
    when(pivotProvider.getPivot()).thenReturn(pivot);

    // when
    String result = wenflonProxyBean.test();

    // then
    Assertions.assertThat(result).isEqualTo(implementationClass.getCanonicalName());
  }

  public static Stream<Arguments> getTestConfigurations() {
    return Stream.of(
        Arguments.of(ServiceA.class, "panda"),
        Arguments.of(ServiceB.class, "grizzly"),
        Arguments.of(ServiceA.class, "koala"),
        Arguments.of(ServiceB.class, "white bear"),
        Arguments.of(ServiceC.class, "value out of the list"));
  }
}

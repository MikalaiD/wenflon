package io.github.mikalaid.wenflon.test.conditions.single_pivot_provider;

import static org.mockito.Mockito.when;

import io.github.mikalaid.wenflon.core.PivotProvider;
import io.github.mikalaid.wenflon.test._common.ServiceA;
import io.github.mikalaid.wenflon.test._common.ServiceB;
import io.github.mikalaid.wenflon.test._common.ServiceC;
import io.github.mikalaid.wenflon.test._common.Testable;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ComponentScan("io.github.mikalaid.wenflon.core")
@ContextConfiguration(classes = {TestConfig.class})
@TestPropertySource("classpath:conditions/application-test_happypath.properties")
class ConditionsTest {

  @Autowired Testable wenflonProxyBean;

  @SpyBean
  PivotProvider<String> pivotProviderSpy;

  @ParameterizedTest
  @MethodSource("getTestConfigurations")
  void condition_work(Class<?> implementationClass, String pivot) {
    // given
    when(pivotProviderSpy.getPivot()).thenReturn(pivot);

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

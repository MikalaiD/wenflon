<<<<<<<< HEAD:core/src/test/java/com/yosik/wenflon/spring_tests/conditions/single_pivot_provider/ConditionsTest.java
package com.yosik.wenflon.spring_tests.conditions.single_pivot_provider;

import static org.mockito.Mockito.when;

import com.yosik.wenflon.Config;
import com.yosik.wenflon.PivotProvider;
import com.yosik.wenflon.WenflonProperties;
import com.yosik.wenflon.spring_tests._common.ServiceA;
import com.yosik.wenflon.spring_tests._common.ServiceB;
import com.yosik.wenflon.spring_tests._common.ServiceC;
import com.yosik.wenflon.spring_tests._common.Testable;
========
package com.github.mikalaid.wenflon.test.conditions.single_pivot_provider;

import static org.mockito.Mockito.when;

import com.github.mikalaid.wenflon.core.PivotProvider;
import com.github.mikalaid.wenflon.test._common.ServiceA;
import com.github.mikalaid.wenflon.test._common.ServiceB;
import com.github.mikalaid.wenflon.test._common.ServiceC;
import com.github.mikalaid.wenflon.test._common.Testable;
>>>>>>>> develop:core/src/test/java/com/github/mikalaid/wenflon/test/conditions/single_pivot_provider/ConditionsTest.java
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
<<<<<<<< HEAD:core/src/test/java/com/yosik/wenflon/spring_tests/conditions/single_pivot_provider/ConditionsTest.java
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.mock.mockito.SpyBean;
========
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.ComponentScan;
>>>>>>>> develop:core/src/test/java/com/github/mikalaid/wenflon/test/conditions/single_pivot_provider/ConditionsTest.java
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
<<<<<<<< HEAD:core/src/test/java/com/yosik/wenflon/spring_tests/conditions/single_pivot_provider/ConditionsTest.java
@EnableConfigurationProperties(WenflonProperties.class)
@ContextConfiguration(classes = {TestConfig.class, Config.class})
========
@ComponentScan("com.github.mikalaid.wenflon.core")
@ContextConfiguration(classes = {TestConfig.class})
>>>>>>>> develop:core/src/test/java/com/github/mikalaid/wenflon/test/conditions/single_pivot_provider/ConditionsTest.java
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

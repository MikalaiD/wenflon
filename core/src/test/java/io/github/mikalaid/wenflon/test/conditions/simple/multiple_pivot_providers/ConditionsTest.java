package io.github.mikalaid.wenflon.test.conditions.simple.multiple_pivot_providers;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import io.github.mikalaid.wenflon.core.PivotProvider;
import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
@TestPropertySource("classpath:conditions/simple/multiple_pivot_providers/application-test.properties")
class ConditionsTest {

  @Autowired
  TestableA wenflonProxyBeanA;
  @Autowired
  TestableB wenflonProxyBeanB;

  @SpyBean
  @Qualifier("providerAlpha")
  PivotProvider<String> pivotProviderAlpha;

  @SpyBean
  @Qualifier("providerBeta")
  PivotProvider<String> pivotProviderBeta;

  @ParameterizedTest
  @MethodSource("getTestConfigurations")
  void conditions_work_with_multiple_providers(Class<?> implementationClassX, String pivotX, Class<?> implementationClassY, String pivotY){
    //given
    when(pivotProviderAlpha.getPivot()).thenReturn(pivotX);
    when(pivotProviderBeta.getPivot()).thenReturn(pivotY);

    //when
    var outputX = wenflonProxyBeanA.test();
    var outputY = wenflonProxyBeanB.test();

    //then
    assertThat(outputX).isEqualTo(implementationClassX.getCanonicalName());
    assertThat(outputY).isEqualTo(implementationClassY.getCanonicalName());
  }

  public static Stream<Arguments> getTestConfigurations() {
    return Stream.of(
            Arguments.of(ServiceA1.class, "orange", ServiceB2.class, "France"),
            Arguments.of(ServiceA2.class, "non-existing-value", ServiceB2.class, "France"),
            Arguments.of(ServiceA2.class, "red", ServiceB1.class, "US"),
            Arguments.of(ServiceA2.class, "red", ServiceB1.class, "US")
            );
  }
}

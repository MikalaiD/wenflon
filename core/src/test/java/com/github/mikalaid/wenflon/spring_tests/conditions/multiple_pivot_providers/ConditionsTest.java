package com.github.mikalaid.wenflon.spring_tests.conditions.multiple_pivot_providers;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.github.mikalaid.wenflon.Config;
import com.github.mikalaid.wenflon.PivotProvider;
import com.github.mikalaid.wenflon.WenflonProperties;
import com.github.mikalaid.wenflon.spring_tests._common.ServiceE;
import com.github.mikalaid.wenflon.spring_tests._common.ServiceF;
import com.github.mikalaid.wenflon.spring_tests._common.ServiceG;
import com.github.mikalaid.wenflon.spring_tests._common.ServiceH;
import com.github.mikalaid.wenflon.spring_tests._common.TestableWithProviderX;
import com.github.mikalaid.wenflon.spring_tests._common.TestableWithProviderY;
import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.mock.mockito.SpyBean;
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

  @SpyBean
  @Qualifier("providerX")
  PivotProvider<String> pivotProviderX;

  @SpyBean
  @Qualifier("providerY")
  PivotProvider<String> pivotProviderY;

  @ParameterizedTest
  @MethodSource("getTestConfigurations")
  void conditions_work_with_multiple_providers(Class<?> implementationClassX, String pivotX, Class<?> implementationClassY, String pivotY){
    //given
    when(pivotProviderX.getPivot()).thenReturn(pivotX);
    when(pivotProviderY.getPivot()).thenReturn(pivotY);

    //when
    var outputX = wenflonProxyBeanX.test();
    var outputY = wenflonProxyBeanY.test();

    //then
    assertThat(outputX).isEqualTo(implementationClassX.getCanonicalName());
    assertThat(outputY).isEqualTo(implementationClassY.getCanonicalName());
  }

  public static Stream<Arguments> getTestConfigurations() {
    return Stream.of(
            Arguments.of(ServiceE.class, "orange", ServiceH.class, "France"),
            Arguments.of(ServiceF.class, "non-existing-value", ServiceH.class, "France"),
            Arguments.of(ServiceF.class, "red", ServiceG.class, "US"),
            Arguments.of(ServiceF.class, "red", ServiceG.class, "US")
            );
  }
}

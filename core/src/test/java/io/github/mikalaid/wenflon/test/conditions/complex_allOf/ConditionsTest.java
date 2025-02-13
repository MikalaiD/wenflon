package io.github.mikalaid.wenflon.test.conditions.complex_allOf;


import io.github.mikalaid.wenflon.core.PivotProvider;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@TestPropertySource("classpath:conditions/complex/allOf/application-test.properties")
@ContextConfiguration(classes = {TestConfig.class})
class ConditionsTest {

    @Autowired
    Testable underTest;

    @SpyBean
    @Qualifier("providerBeta")
    PivotProvider<Integer> pivotProviderBeta;

    @SpyBean
    @Qualifier("providerAlpha")
    PivotProvider<String> pivotProviderAlpha;

    @ParameterizedTest
    @MethodSource("getTestConfigurations")
        //todo write documentation for this happy path
    void conditions_work_with_multiple_providers(String pivotAlpha, Integer pivotBeta, Class<?> implementation) {
        //given
        when(pivotProviderAlpha.getPivot()).thenReturn(pivotAlpha);
        when(pivotProviderBeta.getPivot()).thenReturn(pivotBeta);

        //when
        var output = underTest.test();

        //then
        assertThat(output).isEqualTo(implementation.getCanonicalName());
    }

    public static Stream<Arguments> getTestConfigurations() {
        return Stream.of(
                Arguments.of("panda", 1, ServiceA.class),
                Arguments.of("panda", 42, ServiceA.class),
                Arguments.of("ruanda", 1, ServiceA.class),
                Arguments.of("duck", 404, ServiceB.class));
    }

    //todo think of the following thing: if user chooses complex conditions - shouldn't we enforce declaration of the default
    // implementation? With complexity of conditions increase probability that condition will not match
    // throwing exception in this case seems not a good idea.
}

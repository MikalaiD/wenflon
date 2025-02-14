package io.github.mikalaid.wenflon.test.conditions.complex.anyOf;


import io.github.mikalaid.wenflon.core.PivotProvider;
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

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@TestPropertySource("classpath:conditions/complex/anyOf/application-test.properties")
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
                Arguments.of("panda", 0, ServiceA.class),
                Arguments.of("", 1, ServiceA.class),
                Arguments.of("duck", 0, ServiceB.class),
                Arguments.of("", 404, ServiceB.class),
                Arguments.of("duck", 404, ServiceB.class)
                //todo currently the first is returned if both implementations match
                // describe the case in documentation
                // think of introducing property to stir this behaviour - throw exception if ambiguious or take first match
//                Arguments.of("mammal",0, ServiceA.class)


        );
    }

    //todo think of the following thing: if user chooses complex conditions - shouldn't we enforce declaration of the default
    // implementation? With complexity of conditions increase probability that condition will not match
    // throwing exception in this case seems not a good idea.
}

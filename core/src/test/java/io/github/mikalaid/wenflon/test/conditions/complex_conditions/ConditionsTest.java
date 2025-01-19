package io.github.mikalaid.wenflon.test.conditions.complex_conditions;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import io.github.mikalaid.wenflon.core.PivotProvider;
import io.github.mikalaid.wenflon.test._common.ServiceE;
import io.github.mikalaid.wenflon.test._common.ServiceF;
import io.github.mikalaid.wenflon.test._common.ServiceG;
import io.github.mikalaid.wenflon.test._common.ServiceH;
import io.github.mikalaid.wenflon.test._common.TestableWithProviderX;
import io.github.mikalaid.wenflon.test._common.TestableWithProviderY;
import io.github.mikalaid.wenflon.test.conditions.multiple_pivot_providers.TestConfig;
import java.util.stream.Stream;
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

@ExtendWith(SpringExtension.class)
@ComponentScan("io.github.mikalaid.wenflon.core")
@ContextConfiguration(classes = {TestConfig.class})
@TestPropertySource("classpath:conditions/application-test_complex_conditions.properties")
class ConditionsTest {


}

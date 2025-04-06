package io.github.mikalaid.wenflon.test.bean_creation.multiple_complex_for_same_wenflon;


import io.github.mikalaid.wenflon.exceptions.WenflonException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
class ConditionsTest {

    @Test
    void testWenflonException_ifThereAreMultipleComplexConditionsForSameWenflonInterface() {
        assertThatThrownBy(() -> {
            try (AnnotationConfigApplicationContext context =
                         new AnnotationConfigApplicationContext()) {
                context.getEnvironment().getPropertySources().addFirst(new MapPropertySource("test-props", stubTestProperties()));
                context.register(TestConfig.class);
                context.refresh();
            }
        }).isInstanceOf(WenflonException.class);
    }
    private static HashMap<String, Object> stubTestProperties() {
        final HashMap<String, Object> properties = new HashMap<>();
        properties.put("wenflon.complex-conditions.testableA.anyOf.providerAlpha.values", "panda, ruanda, mammal");
        properties.put("wenflon.complex-conditions.testableA.allOf.providerBeta.values", "1, 42");
        return properties;
    }
}

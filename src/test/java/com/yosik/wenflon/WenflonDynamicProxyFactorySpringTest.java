package com.yosik.wenflon;


import com.yosik.wenflon.test_classes.ServiceA;
import com.yosik.wenflon.test_classes.Testable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class WenflonDynamicProxyFactorySpringTest {


    @TestConfiguration
    static class TestConfig {

        @Bean
        WenflonRegistry wenflonRegistry() {
            return new WenflonRegistry();
        }

        @Bean
        WenflonBeansPostprocessor postprocessor(WenflonRegistry wenflonRegistry) {
            return new WenflonBeansPostprocessor(wenflonRegistry);
        }

        @Bean
        Testable testable() {
            return new ServiceA();
        }
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void simple_test() {
        assert true;
    }

}

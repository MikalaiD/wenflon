package com.yosik.wenflon;


import com.yosik.wenflon.test_classes.ServiceA;
import com.yosik.wenflon.test_classes.Testable;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class WenflonDynamicProxySpringTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        WenflonBeansPostprocessor postprocessor() {
            return new WenflonBeansPostprocessor();
        }

        @Bean
        Testable testable(){
            return new ServiceA();
        }
    }

    @Test
    void simple_test() {
        Assertions.assertThatRuntimeException();
    }

}

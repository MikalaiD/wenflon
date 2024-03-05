package com.yosik.wenflon;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class WenflonSpringTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        WenflonBeansPostprocessor postprocessor() {
            return new WenflonBeansPostprocessor();
        }
    }

    @Test
    void simple_test() {
        assert true;
    }

}

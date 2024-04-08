package com.yosik.wenflon;


import com.yosik.wenflon.test_classes.ServiceA;
import com.yosik.wenflon.test_classes.ServiceB;
import com.yosik.wenflon.test_classes.Testable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
public class WenflonDynamicProxySpringTest {

    @Autowired
    Testable testable;

//    @Autowired
//    InterfaceWithoutImplementation interfaceWithoutImplementation;

    @TestConfiguration
    static class TestConfig {

        @Bean
        WenflonBeanPostprocessor postprocessor() {
            return new WenflonBeanPostprocessor();
        }

        @Bean
        WenflonBeanFactoryPostprocessor factoryPostprocessor() {
            return new WenflonBeanFactoryPostprocessor();
        }

        @Bean
        Testable testableA() {
            return new ServiceA();
        }
        @Bean
        Testable testableB() {
            return new ServiceB();
        }

    }

    @BeforeEach
    void setUp() {

    }

    @Test
    void simple_test() {
        assert testable!=null;
    }

}

package com.yosik.wenflon;


import com.yosik.wenflon.test_classes.ServiceA;
import com.yosik.wenflon.test_classes.ServiceB;
import com.yosik.wenflon.test_classes.Testable;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class WenflonDynamicProxySpringTest {

    @Autowired
    Testable primaryTestable;

    @Autowired
    @Qualifier("testableA")
    Testable testableA;
    @Autowired
    @Qualifier("testableB")
    Testable testableB;

//    @Autowired
//    InterfaceWithoutImplementation interfaceWithoutImplementation;

    @TestConfiguration
    static class TestConfig {

        @Bean
        WenflonBeanPostprocessor factoryPostprocessor() {
            return new WenflonBeanPostprocessor();
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
    void beans_are_created_correctly() {
        Assertions.assertThat(primaryTestable).isNotNull();
        Assertions.assertThat(testableA).isNotNull();
        Assertions.assertThat(testableB).isNotNull();
        Assertions.assertThat(testableA).isNotEqualTo(testableB);
        Assertions.assertThat(testableA).isNotEqualTo(primaryTestable);
        Assertions.assertThat(testableB).isNotEqualTo(primaryTestable);
    }


    @Test
    void condition_work(){
        String result = primaryTestable.test();
        Assertions.assertThat(result).isEqualTo(ServiceB.class.getCanonicalName());
    }
}

package com.yosik.wenflon.spring_tests;


import com.yosik.wenflon.PivotProvider;
import com.yosik.wenflon.WenflonBeanPostprocessor;
import com.yosik.wenflon.spring_tests.test_classes.ServiceA;
import com.yosik.wenflon.spring_tests.test_classes.ServiceB;
import com.yosik.wenflon.spring_tests.test_classes.Testable;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class ConditionsTest { //todo extract config as in bean creation test

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

        @Bean
        PivotProvider<String> defaultPivotProvider(){
            return ()->"panda";
        }
    }

    @Test
    void condition_work(){
        String result = primaryTestable.test();
        Assertions.assertThat(result).isEqualTo(ServiceA.class.getCanonicalName());
    }
}

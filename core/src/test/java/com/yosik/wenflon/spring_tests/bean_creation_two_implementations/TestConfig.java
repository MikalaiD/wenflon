package com.yosik.wenflon.spring_tests.bean_creation_two_implementations;

import com.yosik.wenflon.*;
import com.yosik.wenflon.spring_tests._common.ClassAsWenflon;
import com.yosik.wenflon.spring_tests._common.ServiceA;
import com.yosik.wenflon.spring_tests._common.ServiceB;
import com.yosik.wenflon.spring_tests._common.Testable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {

    @Bean
    static WenflonBeanPostprocessor factoryPostprocessor() {
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

    @Bean
    ClassAsWenflon serviceMarkedAsWenflon(){
        return new ClassAsWenflon();
    }
}

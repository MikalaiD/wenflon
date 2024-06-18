package com.yosik.wenflon.spring_tests.conditions;

import com.yosik.wenflon.*;
import com.yosik.wenflon.spring_tests._common.ServiceA;
import com.yosik.wenflon.spring_tests._common.ServiceB;
import com.yosik.wenflon.spring_tests._common.Testable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {

    @Bean //todo remove since it is configuration?
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
}

package com.yosik.wenflon.spring_tests.conditions;

import com.yosik.wenflon.WenflonBeanPostProcessor;
import com.yosik.wenflon.spring_tests._common.ServiceA;
import com.yosik.wenflon.spring_tests._common.ServiceB;
import com.yosik.wenflon.spring_tests._common.Testable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(WenflonBeanPostProcessor.class)
public class TestConfig {

    @Bean
    Testable testableA() {
        return new ServiceA();
    }
    @Bean
    Testable testableB() {
        return new ServiceB();
    }
}

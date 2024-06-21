package com.yosik.wenflon.spring_tests.bean_creation_one_implementation;

import com.yosik.wenflon.*;
import com.yosik.wenflon.spring_tests._common.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(WenflonBeanPostProcessor.class)
public class TestConfig {

    @Bean
    Testable testableA(){
    return new ServiceA();
    }

    @Bean
    PivotProvider<String> defaultPivotProvider(){
        return ()->"panda";
    }
}

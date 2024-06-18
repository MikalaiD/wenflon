package com.yosik.wenflon.spring_tests.bean_creation_one_implementation;

import com.yosik.wenflon.*;
import com.yosik.wenflon.spring_tests._common.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {

    @Bean
    static WenflonBeanPostprocessor factoryPostprocessor() {
        return new WenflonBeanPostprocessor();
    }

    @Bean
    Testable testableA(){
    return new ServiceA();
    }

    @Bean
    PivotProvider<String> defaultPivotProvider(){
        return ()->"panda";
    }
}

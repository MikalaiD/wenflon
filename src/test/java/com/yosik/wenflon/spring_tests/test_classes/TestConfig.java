package com.yosik.wenflon.spring_tests.test_classes;

import com.yosik.wenflon.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class TestConfig {

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

    @Bean
    FinalAssembler finalAssembler(List<WenflonDynamicProxy<?>> wenflonDynamicProxies, WenflonProperties properties){
        return new FinalAssembler(wenflonDynamicProxies, properties);
    }
}

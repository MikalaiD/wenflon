package com.yosik.wenflon.spring_tests.bean_creation;

import com.yosik.wenflon.*;
import com.yosik.wenflon.spring_tests.common.ServiceA;
import com.yosik.wenflon.spring_tests.common.ServiceB;
import com.yosik.wenflon.spring_tests.common.Testable;
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
    FinalAssembler finalAssembler(List<WenflonDynamicProxy<?>> wenflonDynamicProxies, WenflonProperties properties,
                                  List<PivotProvider<?>> pivotProviders){
        return new FinalAssembler(wenflonDynamicProxies, properties, pivotProviders);
    }
}

package com.yosik.wenflon.spring_tests.conditions;

import com.yosik.wenflon.*;
import com.yosik.wenflon.spring_tests.common.ServiceA;
import com.yosik.wenflon.spring_tests.common.ServiceB;
import com.yosik.wenflon.spring_tests.common.Testable;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
    FinalAssembler finalAssembler(List<WenflonDynamicProxy<?>> wenflonDynamicProxies, WenflonProperties properties,
                                  List<PivotProvider<?>> pivotProviders){
        return new FinalAssembler(wenflonDynamicProxies, properties, pivotProviders);
    }
}

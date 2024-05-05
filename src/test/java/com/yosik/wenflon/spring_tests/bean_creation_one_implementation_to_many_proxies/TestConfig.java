package com.yosik.wenflon.spring_tests.bean_creation_one_implementation_to_many_proxies;

import com.yosik.wenflon.*;
import com.yosik.wenflon.spring_tests._common.*;

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
    ServiceB testableB() {
        return new ServiceB();
    }

    @Bean
    TestableV2 testableC() {
        return new ServiceC();
    }

    @Bean
    PivotProvider<String> defaultPivotProvider(){
        return ()->"grizzly";
    }


    @Bean
    FinalAssembler finalAssembler(List<ProxyFactory<?>> wenflonDynamicProxies, DynamicProxyProperties properties,
                                  List<PivotProvider<?>> pivotProviders){
        return new FinalAssembler(wenflonDynamicProxies, properties, pivotProviders);
    }
}

package com.yosik.wenflon.spring_tests.bean_creation_one_implementation;

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
    Testable testableA(){
    return new ServiceA();
    }

    @Bean
    PivotProvider<String> defaultPivotProvider(){
        return ()->"panda";
    }


    @Bean
    FinalAssembler finalAssembler(List<ProxyFactory<?>> wenflonDynamicProxies, DynamicProxyProperties properties,
                                  List<PivotProvider<?>> pivotProviders){
        return new FinalAssembler(wenflonDynamicProxies, properties, pivotProviders);
    }
}

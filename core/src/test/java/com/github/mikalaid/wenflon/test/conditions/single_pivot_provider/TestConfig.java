package com.github.mikalaid.wenflon.test.conditions.single_pivot_provider;

import com.github.mikalaid.wenflon.core.PivotProvider;
import com.github.mikalaid.wenflon.test._common.ServiceA;
import com.github.mikalaid.wenflon.test._common.ServiceB;
import com.github.mikalaid.wenflon.test._common.ServiceC;
import com.github.mikalaid.wenflon.test._common.StubPivotProvider;
import com.github.mikalaid.wenflon.test._common.Testable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.github.mikalaid.wenflon.core")
public class TestConfig {

    @Bean
    Testable testableA() {
        return new ServiceA();
    }
    @Bean
    Testable testableB() {
        return new ServiceB();
    }
    @Bean
    Testable testableC() {return new ServiceC();}

    @Bean
    PivotProvider<String> stubPivotProvider(){
        return new StubPivotProvider();
    }
}

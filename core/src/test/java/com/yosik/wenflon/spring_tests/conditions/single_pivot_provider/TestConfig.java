package com.yosik.wenflon.spring_tests.conditions.single_pivot_provider;

import com.yosik.wenflon.Config;
import com.yosik.wenflon.PivotProvider;
import com.yosik.wenflon.spring_tests._common.ServiceA;
import com.yosik.wenflon.spring_tests._common.ServiceB;
import com.yosik.wenflon.spring_tests._common.ServiceC;
import com.yosik.wenflon.spring_tests._common.StubPivotProvider;
import com.yosik.wenflon.spring_tests._common.Testable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(Config.class)
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

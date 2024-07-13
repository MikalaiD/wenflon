package com.yosik.wenflon.spring_tests.conditions.multiple_pivot_providers;

import com.yosik.wenflon.Config;
import com.yosik.wenflon.spring_tests._common.ServiceA;
import com.yosik.wenflon.spring_tests._common.ServiceB;
import com.yosik.wenflon.spring_tests._common.ServiceC;
import com.yosik.wenflon.spring_tests._common.ServiceE;
import com.yosik.wenflon.spring_tests._common.ServiceF;
import com.yosik.wenflon.spring_tests._common.ServiceG;
import com.yosik.wenflon.spring_tests._common.Testable;
import com.yosik.wenflon.spring_tests._common.TestableWithProviderX;
import com.yosik.wenflon.spring_tests._common.TestableWithProviderY;
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
    TestableWithProviderX testableE() {
        return new ServiceE();
    }
    @Bean
    TestableWithProviderX testableF() {return new ServiceF();}

    @Bean
    TestableWithProviderY testableG() {return new ServiceG();}
}

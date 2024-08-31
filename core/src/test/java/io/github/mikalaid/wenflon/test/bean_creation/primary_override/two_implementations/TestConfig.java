package io.github.mikalaid.wenflon.test.bean_creation.primary_override.two_implementations;

import io.github.mikalaid.wenflon.core.PivotProvider;
import io.github.mikalaid.wenflon.test._common.ClassAsWenflon;
import io.github.mikalaid.wenflon.test._common.ServiceA;
import io.github.mikalaid.wenflon.test._common.ServiceB;
import io.github.mikalaid.wenflon.test._common.Testable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@ComponentScan("io.github.mikalaid.wenflon.core")
public class TestConfig {

    @Bean
    Testable testableA() {
        return new ServiceA();
    }
    @Bean
    @Primary
    Testable testableB() {
        return new ServiceB();
    }

    @Bean
    PivotProvider<String> defaultPivotProvider(){
        return ()->"panda";
    }

    @Bean
    ClassAsWenflon serviceMarkedAsWenflon(){
        return new ClassAsWenflon();
    }
}

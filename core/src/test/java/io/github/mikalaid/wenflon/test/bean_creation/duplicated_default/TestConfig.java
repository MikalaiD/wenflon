package io.github.mikalaid.wenflon.test.bean_creation.duplicated_default;

import io.github.mikalaid.wenflon.core.PivotProvider;
import io.github.mikalaid.wenflon.test._common.ServiceB;
import io.github.mikalaid.wenflon.test._common.ServiceC;
import io.github.mikalaid.wenflon.test._common.Testable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("io.github.mikalaid.wenflon.core")
public class TestConfig {

    @Bean
    Testable testableB() {
        return new ServiceB();
    }

    @Bean
    Testable testableC() {return new ServiceC();}
    @Bean
    PivotProvider<String> defaultPivotProvider(){
        return ()->"panda";
    }
}

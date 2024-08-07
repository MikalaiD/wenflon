package com.github.mikalaid.wenflon.spring_tests.bean_creation.duplicated_default;

import com.github.mikalaid.wenflon.spring_tests._common.ServiceC;
import com.github.mikalaid.wenflon.Config;
import com.github.mikalaid.wenflon.PivotProvider;
import com.github.mikalaid.wenflon.WenflonProperties;
import com.github.mikalaid.wenflon.spring_tests._common.ServiceB;
import com.github.mikalaid.wenflon.spring_tests._common.Testable;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(Config.class)
@EnableConfigurationProperties(WenflonProperties.class)
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

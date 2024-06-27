package com.yosik.wenflon.spring_tests.duplicated_default;

import com.yosik.wenflon.PivotProvider;
import com.yosik.wenflon.WenflonBeanPostProcessor;
import com.yosik.wenflon.WenflonProperties;
import com.yosik.wenflon.spring_tests._common.ClassAsWenflon;
import com.yosik.wenflon.spring_tests._common.ServiceA;
import com.yosik.wenflon.spring_tests._common.Testable;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(WenflonBeanPostProcessor.class)
@EnableConfigurationProperties(WenflonProperties.class)
public class TestConfig {

    @Bean
    Testable testableA() {
        return new ServiceA();
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

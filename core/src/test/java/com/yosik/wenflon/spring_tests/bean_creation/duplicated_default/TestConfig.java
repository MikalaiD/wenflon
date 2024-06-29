package com.yosik.wenflon.spring_tests.bean_creation.duplicated_default;

import com.yosik.wenflon.PivotProvider;
import com.yosik.wenflon.WenflonBeanPostProcessor;
import com.yosik.wenflon.WenflonProperties;
import com.yosik.wenflon.spring_tests._common.ClassAsWenflon;
import com.yosik.wenflon.spring_tests._common.ServiceA;
import com.yosik.wenflon.spring_tests._common.ServiceB;
import com.yosik.wenflon.spring_tests._common.ServiceC;
import com.yosik.wenflon.spring_tests._common.Testable;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

@Configuration
@Import(WenflonBeanPostProcessor.class)
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

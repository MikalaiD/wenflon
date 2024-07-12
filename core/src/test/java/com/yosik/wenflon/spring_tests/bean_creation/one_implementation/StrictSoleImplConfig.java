package com.yosik.wenflon.spring_tests.bean_creation.one_implementation;

import com.yosik.wenflon.*;
import com.yosik.wenflon.spring_tests._common.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(Config.class)
public class StrictSoleImplConfig {

    @Bean
    TestableStrict testableD(){
    return new ServiceD();
    }

    @Bean
    PivotProvider<String> defaultPivotProvider(){
        return ()->"panda";
    }
}

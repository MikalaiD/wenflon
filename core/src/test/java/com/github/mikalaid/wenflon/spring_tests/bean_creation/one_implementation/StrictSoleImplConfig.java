package com.github.mikalaid.wenflon.spring_tests.bean_creation.one_implementation;

import com.github.mikalaid.wenflon.Config;
import com.github.mikalaid.wenflon.PivotProvider;
import com.github.mikalaid.wenflon.spring_tests._common.ServiceD;
import com.github.mikalaid.wenflon.spring_tests._common.TestableStrict;
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

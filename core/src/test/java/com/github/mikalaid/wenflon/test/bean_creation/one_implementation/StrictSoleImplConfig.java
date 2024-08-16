package com.github.mikalaid.wenflon.test.bean_creation.one_implementation;

import com.github.mikalaid.wenflon.core.PivotProvider;
import com.github.mikalaid.wenflon.test._common.ServiceD;
import com.github.mikalaid.wenflon.test._common.TestableStrict;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.github.mikalaid.wenflon.core")
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

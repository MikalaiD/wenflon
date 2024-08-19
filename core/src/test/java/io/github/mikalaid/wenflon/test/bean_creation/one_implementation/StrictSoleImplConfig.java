package io.github.mikalaid.wenflon.test.bean_creation.one_implementation;

import io.github.mikalaid.wenflon.core.PivotProvider;
import io.github.mikalaid.wenflon.test._common.ServiceD;
import io.github.mikalaid.wenflon.test._common.TestableStrict;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("io.github.mikalaid.wenflon.core")
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

package io.github.mikalaid.wenflon.test.bean_creation.prototype;

import io.github.mikalaid.wenflon.core.PivotProvider;
import io.github.mikalaid.wenflon.test._common.ClassAsWenflon;
import io.github.mikalaid.wenflon.test._common.ServiceA;
import io.github.mikalaid.wenflon.test._common.ServiceB;
import io.github.mikalaid.wenflon.test._common.Testable;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

@Configuration
@ComponentScan("io.github.mikalaid.wenflon.core")
public class TestConfig {

    @Bean
    @Scope(scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.INTERFACES)
    Testable testableA() {
        return new ServiceA();
    }
    @Bean
    @Scope(scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
    Testable testableB() {
        return new ServiceB();
    }

    @Bean
    PivotProvider<String> defaultPivotProvider(){
        return ()->"panda";
    }
}

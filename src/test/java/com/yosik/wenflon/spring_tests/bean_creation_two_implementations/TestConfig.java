<<<<<<<< HEAD:src/test/java/com/yosik/wenflon/spring_tests/bean_creation_two_implementations/TestConfig.java
package com.yosik.wenflon.spring_tests.bean_creation_two_implementations;

import com.yosik.wenflon.*;
import com.yosik.wenflon.spring_tests._common.ServiceA;
import com.yosik.wenflon.spring_tests._common.ClassAsWenflon;
import com.yosik.wenflon.spring_tests._common.ServiceB;
import com.yosik.wenflon.spring_tests._common.Testable;
========
package com.github.mikalaid.wenflon.test.bean_creation.two_implementations;

import com.github.mikalaid.wenflon.core.PivotProvider;
import com.github.mikalaid.wenflon.test._common.ClassAsWenflon;
import com.github.mikalaid.wenflon.test._common.ServiceA;
import com.github.mikalaid.wenflon.test._common.ServiceB;
import com.github.mikalaid.wenflon.test._common.Testable;
>>>>>>>> develop:core/src/test/java/com/github/mikalaid/wenflon/test/bean_creation/two_implementations/TestConfig.java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
<<<<<<<< HEAD:src/test/java/com/yosik/wenflon/spring_tests/bean_creation_two_implementations/TestConfig.java

import java.util.List;

@Configuration
========

@Configuration
@ComponentScan("com.github.mikalaid.wenflon.core")
>>>>>>>> develop:core/src/test/java/com/github/mikalaid/wenflon/test/bean_creation/two_implementations/TestConfig.java
public class TestConfig {

    @Bean
    WenflonBeanPostprocessor factoryPostprocessor() {
        return new WenflonBeanPostprocessor();
    }

    @Bean
    Testable testableA() {
        return new ServiceA();
    }
    @Bean
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

    @Bean
    FinalAssembler finalAssembler(List<ProxyFactory<?>> wenflonDynamicProxies, DynamicProxyProperties properties,
                                  List<PivotProvider<?>> pivotProviders){
        return new FinalAssembler(wenflonDynamicProxies, properties, pivotProviders);
    }
}

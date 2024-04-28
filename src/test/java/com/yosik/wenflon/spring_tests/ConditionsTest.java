package com.yosik.wenflon.spring_tests;


import com.yosik.wenflon.WenflonProperties;
import com.yosik.wenflon.spring_tests.test_classes.ServiceA;
import com.yosik.wenflon.spring_tests.test_classes.TestConfig;
import com.yosik.wenflon.spring_tests.test_classes.Testable;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties(WenflonProperties.class)
@ContextConfiguration(classes = TestConfig.class)
@TestPropertySource("classpath:application-test.properties")
public class ConditionsTest { 

    @Autowired
    Testable primaryTestable;

    @Autowired
    @Qualifier("testableA")
    Testable testableA;
    @Autowired
    @Qualifier("testableB")
    Testable testableB;
    
    @Test
    void condition_work(){
        String result = primaryTestable.test();
        Assertions.assertThat(result).isEqualTo(ServiceA.class.getCanonicalName());
    }
}

package com.yosik.wenflon.spring_tests.conditions;


import com.yosik.wenflon.PivotProvider;
import com.yosik.wenflon.WenflonProperties;
import com.yosik.wenflon.spring_tests.common.ServiceA;
import com.yosik.wenflon.spring_tests.common.Testable;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties(WenflonProperties.class)
@ContextConfiguration(classes = TestConfig.class)
@TestPropertySource("classpath:conditions/application-test.properties")
public class ConditionsTest { 

    @Autowired
    Testable primaryTestable;

    @Autowired
    @Qualifier("testableA")
    Testable testableA;
    @Autowired
    @Qualifier("testableB")
    Testable testableB;

    @MockBean
    PivotProvider<String> pivotProvider;
    
    @Test
    void condition_work(){
        when(pivotProvider.getPivot()).thenReturn("panda");
        String result = primaryTestable.test();
        Assertions.assertThat(result).isEqualTo(ServiceA.class.getCanonicalName());
    }
}

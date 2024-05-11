package com.yosik.wenflon.spring_tests.bean_creation_one_implementation;

import com.yosik.wenflon.*;
import com.yosik.wenflon.spring_tests._common.ServiceA;
import com.yosik.wenflon.spring_tests._common.Testable;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
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
public class SingleBeanCreationTest {

  @Autowired Testable primaryTestable;
  @Autowired
  @Qualifier("testableA")
  Testable testableA;


  @Autowired FinalAssembler finalAssembler;

  @Autowired WenflonProperties properties;

  @Nested
  @TestPropertySource("classpath:bean_creation_one_implementation/application-test-empty.properties")
  class TestWithNoPropertiesAtAll {

    @Test()
    void beans_are_created_correctly() {
      Assertions.assertThat(primaryTestable).isNotNull();
      Assertions.assertThat(primaryTestable).isInstanceOf(Proxy.class);
      Assertions.assertThat(testableA).isNotNull();
      Assertions.assertThat(testableA).isNotInstanceOf(Proxy.class);
      Assertions.assertThat(testableA).isNotEqualTo(primaryTestable);
      Assertions.assertThat(properties.getConditions()).isNull();
      Assertions.assertThat(finalAssembler).isNotNull();
    }

    @Test
    void basic_call_works(){
      Assertions.assertThat(primaryTestable.test()).isEqualTo(ServiceA.class.getCanonicalName());
    }
  }

  @Nested
  @TestPropertySource("classpath:bean_creation_one_implementation/application-test-always-false.properties")
  class TestWithConditionToFalse_DefaultBehaviour_TheBeanStillReturned{

    @Test()
    void beans_are_created_correctly() {
      Assertions.assertThat(primaryTestable).isNotNull();
      Assertions.assertThat(primaryTestable).isInstanceOf(Proxy.class);
      Assertions.assertThat(testableA).isNotNull();
      Assertions.assertThat(testableA).isNotInstanceOf(Proxy.class);
      Assertions.assertThat(testableA).isNotEqualTo(primaryTestable);
      Assertions.assertThat(properties.getConditions()).isNull();
      Assertions.assertThat(finalAssembler).isNotNull();
    }

    @Test
    void basic_call_works(){
      Assertions.assertThat(primaryTestable.test()).isEqualTo(ServiceA.class.getCanonicalName());
    }
  }

  //TODO test for strict validation
}
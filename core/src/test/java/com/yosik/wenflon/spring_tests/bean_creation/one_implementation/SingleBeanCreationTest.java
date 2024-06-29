package com.yosik.wenflon.spring_tests.bean_creation.one_implementation;

import com.yosik.wenflon.*;
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
@ContextConfiguration(classes = {TestConfig.class, FinalAssemblerConfig.class})
public class SingleBeanCreationTest {

  @Autowired Testable primaryTestable;

  @Autowired
  @Qualifier("testableA")
  Testable testableA;

  @Autowired WenflonProperties properties;

  @Nested
  @TestPropertySource(
      "classpath:bean_creation_one_implementation/application-test-empty.properties")
  class TestWithNoPropertiesAtAll {

    @Test()
    void beans_are_created_correctly() {
      Assertions.assertThat(primaryTestable).isNotNull().isInstanceOf(Proxy.class);
      Assertions.assertThat(testableA)
          .isNotNull()
          .isNotInstanceOf(Proxy.class)
          .isNotEqualTo(primaryTestable);
      Assertions.assertThat(properties.getConditions()).isNull();
    }

    //    @Test
    //    void basic_call_works(){
    //
    // Assertions.assertThat(primaryTestable.test()).isEqualTo(ServiceA.class.getCanonicalName());
    //    }
    // todo #15 add functionality
    // no conditions - one implementation -> add it to default
    // there are conditions - no default impl by default, must expicitely specify it
    // add test for the exception
  }

  @Nested
  @TestPropertySource(
      "classpath:bean_creation_one_implementation/application-test-always-false.properties")
  class TestWithConditionToFalse_DefaultBehaviour_TheBeanStillReturned {

    @Test()
    void beans_are_created_correctly() {
      Assertions.assertThat(primaryTestable).isNotNull().isInstanceOf(Proxy.class);
      Assertions.assertThat(testableA)
          .isNotNull()
          .isNotInstanceOf(Proxy.class)
          .isNotEqualTo(primaryTestable);
      Assertions.assertThat(properties.getConditions()).isNull();
    }
  }

  // TODO test for strict validation
}

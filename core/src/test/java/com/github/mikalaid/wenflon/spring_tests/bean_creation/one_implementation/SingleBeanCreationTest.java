package com.github.mikalaid.wenflon.spring_tests.bean_creation.one_implementation;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.mikalaid.wenflon.Config;
import com.github.mikalaid.wenflon.WenflonProperties;
import com.github.mikalaid.wenflon.exceptions.WenflonException;
import com.github.mikalaid.wenflon.spring_tests._common.ServiceA;
import com.github.mikalaid.wenflon.spring_tests._common.Testable;
import com.github.mikalaid.wenflon.spring_tests._common.TestableStrict;
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
class SingleBeanCreationTest {


  @Nested
  @TestPropertySource(
      "classpath:bean_creation_one_implementation/application-test-empty.properties")
  @ContextConfiguration(classes = {StdConfig.class})
  class TestWithNoPropertiesAtAll {
    @Autowired Testable primaryTestable;

    @Autowired
    @Qualifier("testableA")
    Testable testableA;

    @Autowired WenflonProperties properties;

    @Test()
    void beans_are_created_correctly() {
      Assertions.assertThat(primaryTestable).isNotNull();
      Assertions.assertThat(testableA)
          .isNotNull()
          .isNotEqualTo(primaryTestable);
      Assertions.assertThat(properties.getConditions()).isNull();
    }

    @Test
    void basic_call_works_sole_declared_impl_is_chosen_as_default() {
      Assertions.assertThat(primaryTestable.test()).isEqualTo(ServiceA.class.getCanonicalName());
    }
  }

  @Nested
  @TestPropertySource(
      "classpath:bean_creation_one_implementation/application-test-always-false.properties")
  @ContextConfiguration(classes = {StdConfig.class})
  class TestWithConditionToFalse_DefaultBehaviour_TheBeanStillReturned {
    @Autowired Testable primaryTestable;

    @Autowired
    @Qualifier("testableA")
    Testable testableA;

    @Autowired WenflonProperties properties;

    @Test()
    void beans_are_created_correctly() {
      Assertions.assertThat(primaryTestable).isNotNull();
      Assertions.assertThat(testableA)
          .isNotNull()
          .isNotEqualTo(primaryTestable);
      Assertions.assertThat(properties.getConditions()).isNotNull();
    }
    @Test
    void basic_call_works_sole_conditional_impl_is_chosen_as_default() {
      Assertions.assertThat(primaryTestable.test()).isEqualTo(ServiceA.class.getCanonicalName());
    }
  }
  @Nested
  @TestPropertySource(
      "classpath:bean_creation_one_implementation/application-test-sole-not-default.properties")
  @ContextConfiguration(classes = {StrictSoleImplConfig.class, Config.class})
  class TestWithConditionToFalse_SoleBeanAsDefaultIsFalse_exception_is_thrown {
    @Autowired TestableStrict primaryTestable;

    @Autowired
    @Qualifier("testableD")
    TestableStrict testableD;

    @Autowired WenflonProperties properties;

    @Test()
    void beans_are_created_correctly() {
      Assertions.assertThat(primaryTestable).isNotNull();
      Assertions.assertThat(testableD)
          .isNotNull()
          .isNotEqualTo(primaryTestable);
      Assertions.assertThat(properties.getConditions()).isNotNull();
    }
    @Test
    void basic_call_throws_exception_since_there_is_strict_validation() {
      assertThrows(WenflonException.class, ()->primaryTestable.test());
    }
  }

}

package io.github.mikalaid.wenflon.test.bean_creation.one_implementation;

import static org.junit.jupiter.api.Assertions.assertThrows;

import io.github.mikalaid.wenflon.exceptions.WenflonException;
import io.github.mikalaid.wenflon.test._common.ServiceA;
import io.github.mikalaid.wenflon.test._common.Testable;
import io.github.mikalaid.wenflon.test._common.TestableStrict;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ComponentScan("io.github.mikalaid.wenflon.core")
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


    @Test()
    void beans_are_created_correctly() {
      Assertions.assertThat(primaryTestable).isNotNull();
      Assertions.assertThat(testableA)
          .isNotNull()
          .isNotEqualTo(primaryTestable);
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

    @Test()
    void beans_are_created_correctly() {
      Assertions.assertThat(primaryTestable).isNotNull();
      Assertions.assertThat(testableA)
          .isNotNull()
          .isNotEqualTo(primaryTestable);
    }
    @Test
    void basic_call_works_sole_conditional_impl_is_chosen_as_default() {
      Assertions.assertThat(primaryTestable.test()).isEqualTo(ServiceA.class.getCanonicalName());
    }
  }
  @Nested
  @TestPropertySource(
      "classpath:bean_creation_one_implementation/application-test-sole-not-default.properties")
  @ContextConfiguration(classes = {StrictSoleImplConfig.class})
  @ComponentScan("io.github.mikalaid.wenflon.core")
  class TestWithConditionToFalse_SoleBeanAsDefaultIsFalse_exception_is_thrown {
    @Autowired TestableStrict primaryTestable;

    @Autowired
    @Qualifier("testableD")
    TestableStrict testableD;

    @Test()
    void beans_are_created_correctly() {
      Assertions.assertThat(primaryTestable).isNotNull();
      Assertions.assertThat(testableD)
          .isNotNull()
          .isNotEqualTo(primaryTestable);
    }
    @Test
    void basic_call_throws_exception_since_there_is_strict_validation() {
      assertThrows(WenflonException.class, ()->primaryTestable.test());
    }
  }

}

package io.github.mikalaid.wenflon.test.bean_creation.prototype;


import io.github.mikalaid.wenflon.test._common.ClassAsWenflon;
import io.github.mikalaid.wenflon.test._common.Testable;
import io.github.mikalaid.wenflon.test.bean_creation.primary_override.TestConfig;
import org.assertj.core.api.Assertions;
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
@ContextConfiguration(classes = {TestConfig.class})
@TestPropertySource("classpath:bean_creation_two_implementations/application-test.properties")
class BeanCreationTest {

  @Autowired Testable primaryTestable;

  @Autowired
  @Qualifier("testableA")
  Testable testableA;

  @Autowired
  @Qualifier("testableB")
  Testable testableB;

  @Test()
  void beans_are_created_correctly() {
    Assertions.assertThat(primaryTestable).isNotNull();
    Assertions.assertThat(testableA)
        .isNotNull()
        .isNotEqualTo(testableB)
        .isNotEqualTo(primaryTestable);
    Assertions.assertThat(testableB)
        .isNotNull()
        .isNotEqualTo(primaryTestable);
  }
}
